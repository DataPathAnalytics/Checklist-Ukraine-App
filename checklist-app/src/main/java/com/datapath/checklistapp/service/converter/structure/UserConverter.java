package com.datapath.checklistapp.service.converter.structure;

import com.datapath.checklistapp.dao.entity.DepartmentEntity;
import com.datapath.checklistapp.dao.entity.EmploymentEntity;
import com.datapath.checklistapp.dao.entity.UserEntity;
import com.datapath.checklistapp.dto.DepartmentDTO;
import com.datapath.checklistapp.dto.UserDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserConverter {

    public UserDTO map(UserEntity entity, Optional<EmploymentEntity> lastEmployment) {
        UserDTO dto = new UserDTO();
        BeanUtils.copyProperties(entity, dto);

        lastEmployment.ifPresent(
                employment -> dto.setDepartment(map(employment.getDepartment()))
        );

        dto.setPermissionId(entity.getPermission().getPermissionId());

        return dto;
    }

    private static DepartmentDTO map(DepartmentEntity entity) {
        return new DepartmentDTO(entity.getIdentifier(), entity.getName());
    }
}
