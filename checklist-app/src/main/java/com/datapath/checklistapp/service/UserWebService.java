package com.datapath.checklistapp.service;

import com.datapath.checklistapp.dao.entity.DepartmentEntity;
import com.datapath.checklistapp.dao.entity.EmploymentEntity;
import com.datapath.checklistapp.dao.entity.UserEntity;
import com.datapath.checklistapp.dao.entity.classifier.Permission;
import com.datapath.checklistapp.dao.service.DepartmentDaoService;
import com.datapath.checklistapp.dao.service.UserDaoService;
import com.datapath.checklistapp.dao.service.classifier.PermissionDaoService;
import com.datapath.checklistapp.dto.DepartmentDTO;
import com.datapath.checklistapp.dto.UserDTO;
import com.datapath.checklistapp.dto.UserPageDTO;
import com.datapath.checklistapp.dto.UserStateDTO;
import com.datapath.checklistapp.dto.request.users.ResetPasswordRequest;
import com.datapath.checklistapp.dto.request.users.ResetPasswordSendRequest;
import com.datapath.checklistapp.dto.request.users.UserRegisterRequest;
import com.datapath.checklistapp.dto.request.users.UserUpdateRequest;
import com.datapath.checklistapp.exception.ResetPasswordException;
import com.datapath.checklistapp.exception.UserException;
import com.datapath.checklistapp.security.ConfirmationTokenStorageService;
import com.datapath.checklistapp.security.UsersStorageService;
import com.datapath.checklistapp.service.converter.structure.UserConverter;
import com.datapath.checklistapp.service.notification.EmailNotificationService;
import com.datapath.checklistapp.util.MessageTemplate;
import com.datapath.checklistapp.util.UserUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.datapath.checklistapp.util.Constants.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@AllArgsConstructor
public class UserWebService {

    private final UserDaoService userService;
    private final DepartmentDaoService departmentService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ConfirmationTokenStorageService tokenStorageService;
    private final EmailNotificationService emailSender;
    private final PermissionDaoService permissionService;
    private final UserConverter userConverter;

    public UserPageDTO list(int page, int size) {
        Page<UserEntity> entities = userService.findAll(page, size);

        return new UserPageDTO(
                entities.getTotalElements(),
                entities.getTotalPages(),
                entities.getNumber(),
                entities.getSize(),
                entities.get()
                        .map(this::mapEntityToDTO)
                        .collect(toList())
        );
    }

    @Transactional
    public void register(UserRegisterRequest request) {
        UserEntity user = userService.findByEmail(request.getEmail());
        if (nonNull(user)) {
            if (!user.isRemoved()) throw new UserException("This email already registered");
        } else {
            user = new UserEntity();
        }

        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setDisable(true);
        user.setLocked(true);
        user.setRemoved(false);
        user.setSuperAdmin(false);
        user.setPermission(permissionService.findByRole(AUDITOR_ROLE));

        updateDepartmentRelationship(user, request.getDepartment());

        userService.save(user);

        List<UserEntity> admins = userService.findAdmins();

        if (CollectionUtils.isEmpty(admins)) throw new UserException("Admins not found");

        String textTemplate = String.format(USER_REGISTRATION_MESSAGE_TEMPLATE, user.getFirstName(), user.getLastName(), user.getEmail());

        MessageTemplate template = MessageTemplate.builder()
                .subject(USER_REGISTRATION_MESSAGE_SUBJECT)
                .text(textTemplate)
                .to(admins.stream().map(UserEntity::getEmail).toArray(String[]::new))
                .build();

        emailSender.send(template);
    }

    @Transactional
    public void update(UserUpdateRequest request) {
        UserEntity user = userService.findById(request.getId());

        if (user.isSuperAdmin()) throw new UserException("SuperAdmin updating not allowed");

        if (user.isLocked()) user.setLocked(false);

        MessageTemplate template = null;
        if (nonNull(request.getDisable()) && !request.getDisable().equals(user.isDisable())) {
            user.setDisable(request.getDisable());

            template = MessageTemplate.builder()
                    .subject(ACCOUNT_VERIFICATION_MESSAGE_SUBJECT)
                    .to(new String[]{user.getEmail()})
                    .build();

            if (request.getDisable()) {
                template.setText("Your account has been disabled");

            } else {
                template.setText("Your account has been enabled");
            }
        }

        if (nonNull(request.getDepartment())) {
            updateDepartmentRelationship(user, request.getDepartment());
        }

        if (nonNull(request.getPermissionId())) {
            Permission permission = permissionService.findById(request.getPermissionId());
            if (!ADMIN_ROLE.equals(permission.getRole())) {
                user.setPermission(permission);
            }
        }

        userService.save(user);

        if (nonNull(template)) emailSender.send(template);
        UsersStorageService.removeUser(user.getId());
    }

    @Transactional
    public void delete(Long id) {
        UserEntity user = userService.findById(id);

        if (user.isSuperAdmin()) throw new UserException("SuperAdmin updating not allowed");

        user.setRemoved(true);

        Optional<EmploymentEntity> lastEmployment = getLastEmployment(user.getEmployments());
        lastEmployment.ifPresent(employment -> employment.setEnd(LocalDateTime.now()));

        userService.save(user);
        UsersStorageService.removeUser(user.getId());
    }

    private void updateDepartmentRelationship(UserEntity user, DepartmentDTO departmentDTO) {
        List<EmploymentEntity> employments = user.getEmployments();
        DepartmentEntity department = departmentService.findByIdentifier(departmentDTO.getIdentifier());

        if (isNull(department)) {
            department = new DepartmentEntity();
            department.setIdentifier(departmentDTO.getIdentifier());
        }
        department.setName(departmentDTO.getName());

        if (!isEmpty(employments)) {
            Optional<EmploymentEntity> lastEmployment = getLastEmployment(user.getEmployments());

            if (!lastEmployment.isPresent()) {
                employments.add(new EmploymentEntity(LocalDateTime.now(), null, department));
            } else {
                if (!lastEmployment.get().getDepartment().getIdentifier().equals(departmentDTO.getIdentifier())) {
                    LocalDateTime now = LocalDateTime.now();
                    lastEmployment.get().setEnd(now);
                    employments.add(new EmploymentEntity(now, null, department));
                }
            }
        } else {
            employments.add(new EmploymentEntity(LocalDateTime.now(), null, department));
        }
    }

    private Optional<EmploymentEntity> getLastEmployment(Collection<EmploymentEntity> workPeriods) {
        return workPeriods.stream()
                .filter(d -> isNull(d.getEnd()))
                .findFirst();
    }

    public void sendMailForResetPassword(ResetPasswordSendRequest request) {
        UserEntity user = userService.findByEmail(request.getEmail());

        if (isNull(user)) throw new UserException("User not found");

        String token = UUID.randomUUID().toString();

        String text = String.format(RESET_PASSWORD_MESSAGE_TEMPLATE, request.getPath(), token);

        MessageTemplate message = MessageTemplate.builder()
                .to(new String[]{request.getEmail()})
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
            UserEntity user = userService.findByEmail(email);

            if (nonNull(user)) {
                String encodedNewPassword = passwordEncoder.encode(request.getPassword());
                user.setPassword(encodedNewPassword);
                userService.save(user);
                tokenStorageService.removed(email);
                UsersStorageService.removeUser(user.getId());
            } else {
                throw new UserException("User not updated");
            }

        } else {
            throw new ResetPasswordException("Confirmation token is expired");
        }
    }

    public UserStateDTO getState() {
        boolean exists = userService.existsNotChecked();
        return new UserStateDTO(exists);
    }

    public UserDTO getCurrent() {
        Long currentUserId = UserUtils.getCurrentUserId();
        return mapEntityToDTO(userService.findById(currentUserId));
    }

    private UserDTO mapEntityToDTO(UserEntity entity) {
        return userConverter.map(entity, getLastEmployment(entity.getEmployments()));
    }
}
