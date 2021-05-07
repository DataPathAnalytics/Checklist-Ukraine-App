package com.datapath.checklistapp.service.converter.structure;

import com.datapath.checklistapp.dao.entity.DepartmentEntity;
import com.datapath.checklistapp.dao.entity.EmploymentEntity;
import com.datapath.checklistapp.dao.entity.UserEntity;
import com.datapath.checklistapp.dao.entity.classifier.Permission;
import com.datapath.checklistapp.dto.DepartmentDTO;
import com.datapath.checklistapp.dto.PermissionDTO;
import com.datapath.checklistapp.dto.UserDTO;
import com.datapath.checklistapp.dto.response.export.ExportUserDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Service
public class UserConverter {

    public UserDTO map(UserEntity entity, Optional<EmploymentEntity> lastEmployment) {
        UserDTO dto = new UserDTO();
        BeanUtils.copyProperties(entity, dto);

        lastEmployment.ifPresent(
                employment -> dto.setDepartment(map(employment.getDepartment()))
        );

        dto.setPermissions(map(entity.getPermissions()));

        return dto;
    }

    private Set<PermissionDTO> map(Set<Permission> permissions) {
        return permissions.stream()
                .map(p -> new PermissionDTO(p.getId(), p.getRole(), p.getValue()))
                .collect(toSet());
    }

    private static DepartmentDTO map(DepartmentEntity entity) {
        return new DepartmentDTO(entity.getIdentifier(), entity.getName());
    }

    public ExportUserDTO mapExport(UserEntity entity) {
        ExportUserDTO dto = new ExportUserDTO();

        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setRegisteredDate(entity.getRegisteredDate());
        dto.setDateModified(entity.getDateModified());

        return dto;
    }
}
