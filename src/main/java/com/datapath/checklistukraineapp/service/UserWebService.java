package com.datapath.checklistukraineapp.service;

import com.datapath.checklistukraineapp.dao.node.Department;
import com.datapath.checklistukraineapp.dao.node.User;
import com.datapath.checklistukraineapp.dao.relatioship.UserToDepartment;
import com.datapath.checklistukraineapp.dao.service.DepartmentDaoService;
import com.datapath.checklistukraineapp.dao.service.UserDaoService;
import com.datapath.checklistukraineapp.domain.dto.UserDTO;
import com.datapath.checklistukraineapp.domain.request.UserRegisterRequest;
import com.datapath.checklistukraineapp.domain.request.UserUpdateRequest;
import com.datapath.checklistukraineapp.exception.DepartmentException;
import com.datapath.checklistukraineapp.exception.UserException;
import com.datapath.checklistukraineapp.util.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    public List<UserDTO> list() {
        return userDaoService.findAll()
                .stream()
                .filter(u -> !u.isRemoved())
                .filter(u -> !UserRole.admin.getValue().equals(u.getRole()))
                .map(u -> {
                    UserDTO dto = new UserDTO();
                    BeanUtils.copyProperties(u, dto);

                    Optional<UserToDepartment> lastDepartment = getLastDepartment(u.getDepartments());

                    lastDepartment.ifPresent(
                            userToDepartment -> dto.setDepartmentId(userToDepartment.getDepartment().getId())
                    );

                    return dto;
                }).collect(toList());
    }

    @Transactional
    public void register(UserRegisterRequest request) throws UserException, DepartmentException {
        User existedUser = userDaoService.findByEmail(request.getEmail());
        if (nonNull(existedUser)) throw new UserException("This email already registered");

        LocalDateTime now = LocalDateTime.now();

        User user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setDisable(true);
        user.setLocked(true);
        user.setRemoved(false);
        user.setRole(UserRole.auditor.getValue());
        user.setRegisteredDateTime(now);
        updateDepartmentRelationship(user, now, request.getDepartmentId());

        userDaoService.save(user);
    }

    @Transactional
    public void update(UserUpdateRequest request) throws UserException, DepartmentException {
        User user = userDaoService.findById(request.getId());

        if (user.isLocked()) user.setLocked(false);

        if (nonNull(request.getDisable()) && !request.getDisable().equals(user.isDisable())) {
            user.setDisable(request.getDisable());

            if (request.getDisable()) {
                //send disable notification
            } else {
                //send enable notification
            }
        }

        if (nonNull(request.getDepartmentId())) {
            LocalDateTime now = LocalDateTime.now();
            updateDepartmentRelationship(user, now, request.getDepartmentId());
        }

        if (nonNull(request.getUserRole()) && !request.getUserRole().equals(UserRole.admin)) {
            user.setRole(request.getUserRole().getValue());
        }

        userDaoService.save(user);
    }

    @Transactional
    public void delete(Long id) throws UserException {
        User user = userDaoService.findById(id);
        user.setRemoved(true);

        Optional<UserToDepartment> lastDepartment = getLastDepartment(user.getDepartments());
        lastDepartment.ifPresent(d -> d.setEnd(LocalDateTime.now()));

        userDaoService.save(user);
    }

    private void updateDepartmentRelationship(User user, LocalDateTime date, Long departmentId) throws DepartmentException {
        List<UserToDepartment> departments = user.getDepartments();
        Department department = departmentDaoService.findById(departmentId);

        if (!isEmpty(departments)) {
            Optional<UserToDepartment> lastDepartment = getLastDepartment(user.getDepartments());

            if (!lastDepartment.isPresent()) {
                departments.add(new UserToDepartment(department, date, null));
            } else {
                if (!lastDepartment.get().getDepartment().getId().equals(departmentId)) {
                    lastDepartment.get().setEnd(date);
                    departments.add(new UserToDepartment(department, date, null));
                }
            }
        } else {
            departments.add(new UserToDepartment(department, date, null));
        }
    }

    private Optional<UserToDepartment> getLastDepartment(List<UserToDepartment> departments) {
        return departments.stream()
                .filter(d -> isNull(d.getEnd()))
                .findFirst();
    }
}
