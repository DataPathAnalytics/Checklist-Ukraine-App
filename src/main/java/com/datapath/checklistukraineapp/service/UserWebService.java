package com.datapath.checklistukraineapp.service;

import com.datapath.checklistukraineapp.dao.entity.DepartmentEntity;
import com.datapath.checklistukraineapp.dao.entity.EmploymentEntity;
import com.datapath.checklistukraineapp.dao.entity.PermissionEntity;
import com.datapath.checklistukraineapp.dao.entity.UserEntity;
import com.datapath.checklistukraineapp.dao.service.DepartmentDaoService;
import com.datapath.checklistukraineapp.dao.service.PermissionDaoService;
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
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
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
    private final ConfirmationTokenStorageService tokenStorageService;
    private final EmailSenderService emailSender;
    private final PermissionDaoService permissionService;

    public List<UserDTO> list() {
        return userDaoService.findAll()
                .stream()
                .filter(u -> !u.isRemoved())
                .filter(u -> !ADMIN_ROLE.equals(u.getPermission().getRole()))
                .map(u -> {
                    UserDTO dto = new UserDTO();
                    BeanUtils.copyProperties(u, dto);

                    Optional<EmploymentEntity> lastEmployment = getLastEmployment(u.getEmployments());

                    lastEmployment.ifPresent(
                            employment -> dto.setDepartmentId(employment.getDepartment().getId())
                    );

                    dto.setPermissionId(u.getPermission().getId());

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
        user.setPermission(permissionService.findByRole(AUDITOR_ROLE));
        user.setRegisteredDateTime(now);
        updateDepartmentRelationship(user, now, request.getDepartmentId());

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

        if (ADMIN_ROLE.equals(user.getPermission().getRole())) throw new UserException("Admin updating not allowed");

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

        if (nonNull(request.getDepartmentId())) {
            LocalDateTime now = LocalDateTime.now();
            updateDepartmentRelationship(user, now, request.getDepartmentId());
        }

        if (nonNull(request.getPermissionId())) {
            PermissionEntity permission = permissionService.findById(request.getPermissionId());
            if (!ADMIN_ROLE.equals(permission.getRole())) {
                user.setPermission(permission);
            }
        }

        userDaoService.save(user);

        if (nonNull(template)) emailSender.send(template);
        UsersStorageService.removeUser(user.getId());
    }

    @Transactional
    public void delete(Long id) {
        UserEntity user = userDaoService.findById(id);
        user.setRemoved(true);

        Optional<EmploymentEntity> lastEmployment = getLastEmployment(user.getEmployments());
        lastEmployment.ifPresent(employment -> employment.setEnd(LocalDateTime.now()));

        userDaoService.save(user);
        UsersStorageService.removeUser(user.getId());
    }

    private void updateDepartmentRelationship(UserEntity user, LocalDateTime date, Long departmentId) {
        List<EmploymentEntity> employments = user.getEmployments();
        DepartmentEntity department = departmentDaoService.findById(departmentId);

        if (!isEmpty(employments)) {
            Optional<EmploymentEntity> lastEmployment = getLastEmployment(user.getEmployments());

            if (!lastEmployment.isPresent()) {

                employments.add(new EmploymentEntity(date, null, department));
            } else {
                if (!lastEmployment.get().getDepartment().getId().equals(departmentId)) {
                    lastEmployment.get().setEnd(date);
                    employments.add(new EmploymentEntity(date, null, department));
                }
            }
        } else {
            employments.add(new EmploymentEntity(date, null, department));
        }
    }

    private Optional<EmploymentEntity> getLastEmployment(Collection<EmploymentEntity> workPeriods) {
        return workPeriods.stream()
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
