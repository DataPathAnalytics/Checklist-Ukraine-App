package com.datapath.checklistukraineapp.service;

import com.datapath.checklistukraineapp.dao.entity.DepartmentEntity;
import com.datapath.checklistukraineapp.dao.entity.UserEntity;
import com.datapath.checklistukraineapp.dao.relatioship.UserDepartment;
import com.datapath.checklistukraineapp.dao.service.DepartmentDaoService;
import com.datapath.checklistukraineapp.dao.service.UserDaoService;
import com.datapath.checklistukraineapp.domain.dto.UserDTO;
import com.datapath.checklistukraineapp.domain.request.users.ResetPasswordRequest;
import com.datapath.checklistukraineapp.domain.request.users.ResetPasswordSendRequest;
import com.datapath.checklistukraineapp.domain.request.users.UserRegisterRequest;
import com.datapath.checklistukraineapp.domain.request.users.UserUpdateRequest;
import com.datapath.checklistukraineapp.exception.ResetPasswordException;
import com.datapath.checklistukraineapp.exception.UserException;
import com.datapath.checklistukraineapp.security.ConfirmationTokenStorageService;
import com.datapath.checklistukraineapp.security.UsersStorageService;
import com.datapath.checklistukraineapp.util.MessageTemplate;
import com.datapath.checklistukraineapp.util.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.datapath.checklistukraineapp.util.Constants.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@AllArgsConstructor
public class UserWebService {

    private final UserDaoService userDaoService;
    private final DepartmentDaoService departmentDaoService;
    private final BCryptPasswordEncoder passwordEncoder;
    private ConfirmationTokenStorageService tokenStorageService;
    private EmailSenderService emailSender;

    public List<UserDTO> list() {
        return userDaoService.findAll()
                .stream()
                .filter(u -> !u.isRemoved())
                .filter(u -> !UserRole.admin.getValue().equals(u.getRole()))
                .map(u -> {
                    UserDTO dto = new UserDTO();
                    BeanUtils.copyProperties(u, dto);

                    Optional<UserDepartment> lastDepartment = getLastDepartment(u.getDepartments());

                    lastDepartment.ifPresent(
                            userDepartment -> dto.setDepartment(userDepartment.getDepartment().getRegion())
                    );

                    return dto;
                }).collect(toList());
    }

    @Transactional
    public void register(UserRegisterRequest request) {
        UserEntity existedUser = userDaoService.findByEmail(request.getEmail());
        if (nonNull(existedUser)) throw new UserException("This email already registered");

        LocalDateTime now = LocalDateTime.now();

        UserEntity user = new UserEntity();
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setDisable(true);
        user.setLocked(true);
        user.setRemoved(false);
        user.setRole(UserRole.auditor.getValue());
        user.setRegisteredDateTime(now);
        updateDepartmentRelationship(user, now, request.getDepartment());

        userDaoService.save(user);

        UserEntity admin = userDaoService.findAdmin();

        if (isNull(admin)) throw new UserException("Admin not found");

        String textTemplate = String.format(USER_REGISTRATION_MESSAGE_TEMPLATE, user.getFirstName(), user.getLastName(), user.getEmail());

        MessageTemplate template = MessageTemplate.builder()
                .subject(USER_REGISTRATION_MESSAGE_SUBJECT)
                .text(textTemplate)
                .email(admin.getEmail())
                .build();

        emailSender.send(template);
    }

    @Transactional
    public void update(UserUpdateRequest request) {
        UserEntity user = userDaoService.findById(request.getId());

        if (UserRole.admin.getValue().equals(user.getRole())) throw new UserException("Admin updating not allowed");

        if (user.isLocked()) user.setLocked(false);

        MessageTemplate template = null;
        if (nonNull(request.getDisable()) && !request.getDisable().equals(user.isDisable())) {
            user.setDisable(request.getDisable());

            template = MessageTemplate.builder()
                    .subject(ACCOUNT_VERIFICATION_MESSAGE_SUBJECT)
                    .email(user.getEmail())
                    .build();

            if (request.getDisable()) {
                template.setText("Your account has been disabled");

            } else {
                template.setText("Your account has been enabled");
            }
        }

        if (nonNull(request.getDepartment())) {
            LocalDateTime now = LocalDateTime.now();
            updateDepartmentRelationship(user, now, request.getDepartment());
        }

        if (nonNull(request.getUserRole()) && !request.getUserRole().equals(UserRole.admin)) {
            user.setRole(request.getUserRole().getValue());
        }

        userDaoService.save(user);

        if (nonNull(template)) emailSender.send(template);
        UsersStorageService.removeUser(user.getId());
    }

    @Transactional
    public void delete(Long id) {
        UserEntity user = userDaoService.findById(id);
        user.setRemoved(true);

        Optional<UserDepartment> lastDepartment = getLastDepartment(user.getDepartments());
        lastDepartment.ifPresent(d -> d.setEnd(LocalDateTime.now()));

        userDaoService.save(user);
        UsersStorageService.removeUser(user.getId());
    }

    private void updateDepartmentRelationship(UserEntity user, LocalDateTime date, String departmentRegion) {
        Set<UserDepartment> departments = user.getDepartments();
        DepartmentEntity department = departmentDaoService.findById(departmentRegion);

        if (!isEmpty(departments)) {
            Optional<UserDepartment> lastDepartment = getLastDepartment(user.getDepartments());

            if (!lastDepartment.isPresent()) {
                departments.add(new UserDepartment(department, date, null));
            } else {
                if (!lastDepartment.get().getDepartment().getRegion().equals(departmentRegion)) {
                    lastDepartment.get().setEnd(date);
                    departments.add(new UserDepartment(department, date, null));
                }
            }
        } else {
            departments.add(new UserDepartment(department, date, null));
        }
    }

    private Optional<UserDepartment> getLastDepartment(Set<UserDepartment> departments) {
        return departments.stream()
                .filter(d -> isNull(d.getEnd()))
                .findFirst();
    }

    public void sendMailForResetPassword(ResetPasswordSendRequest request) {
        UserEntity user = userDaoService.findByEmail(request.getEmail());

        if (isNull(user)) throw new UserException("User not found");

        String token = UUID.randomUUID().toString();

        String text = String.format(RESET_PASSWORD_MESSAGE_TEMPLATE, request.getPath(), token);

        MessageTemplate message = MessageTemplate.builder()
                .email(request.getEmail())
                .subject(RESET_PASSWORD_MESSAGE_SUBJECT)
                .text(text)
                .build();

        emailSender.send(message);
        tokenStorageService.add(user.getEmail(), token);
    }

    public void checkTokenForResetPassword(String token) {
        if (!tokenStorageService.isPresent(token)) throw new ResetPasswordException("Confirmation token is expired");
    }

    public void resetPassword(ResetPasswordRequest request) {
        if (tokenStorageService.isPresent(request.getToken())) {
            String email = tokenStorageService.getEmail(request.getToken());
            UserEntity user = userDaoService.findByEmail(email);

            if (nonNull(user)) {
                String encodedNewPassword = passwordEncoder.encode(request.getPassword());
                user.setPassword(encodedNewPassword);
                userDaoService.save(user);
                tokenStorageService.removed(email);
                UsersStorageService.removeUser(user.getId());
            } else {
                throw new UserException("User not updated");
            }

        } else {
            throw new ResetPasswordException("Confirmation token is expired");
        }
    }
}
