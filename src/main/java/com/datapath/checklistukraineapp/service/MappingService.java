package com.datapath.checklistukraineapp.service;

import com.datapath.checklistukraineapp.dao.service.DepartmentDaoService;
import com.datapath.checklistukraineapp.domain.dto.DepartmentDTO;
import com.datapath.checklistukraineapp.domain.response.MappingPrivateResponse;
import com.datapath.checklistukraineapp.domain.response.MappingPublicResponse;
import com.datapath.checklistukraineapp.util.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class MappingService {

    private final DepartmentDaoService departmentDaoService;

    public MappingPublicResponse getPublicMappings() {
        MappingPublicResponse response = new MappingPublicResponse();
        response.setDepartments(
                departmentDaoService.findAll()
                        .stream()
                        .map(d -> {
                            DepartmentDTO dto = new DepartmentDTO();
                            BeanUtils.copyProperties(d, dto);
                            return dto;
                        })
                        .collect(toList())
        );
        return response;
    }

    public MappingPrivateResponse getPrivateMappings() {
        MappingPrivateResponse response = new MappingPrivateResponse();
        response.setRoles(
                Stream.of(UserRole.values())
                        .filter(r -> !r.equals(UserRole.admin))
                        .collect(toList()));
        return response;
    }
}
