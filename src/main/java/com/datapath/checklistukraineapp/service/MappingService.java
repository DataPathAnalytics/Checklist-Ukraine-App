package com.datapath.checklistukraineapp.service;

import com.datapath.checklistukraineapp.dao.service.DepartmentDaoService;
import com.datapath.checklistukraineapp.dao.service.PermissionDaoService;
import com.datapath.checklistukraineapp.domain.dto.DepartmentDTO;
import com.datapath.checklistukraineapp.domain.dto.PermissionDTO;
import com.datapath.checklistukraineapp.domain.response.MappingPrivateResponse;
import com.datapath.checklistukraineapp.domain.response.MappingPublicResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import static com.datapath.checklistukraineapp.util.Constants.ADMIN_ROLE;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class MappingService {

    private final DepartmentDaoService departmentDaoService;
    private final PermissionDaoService permissionDaoService;

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
        response.setPermissions(
                permissionDaoService.findAll().stream()
                        .filter(r -> !ADMIN_ROLE.equals(r.getRole()))
                        .map(r -> {
                            PermissionDTO dto = new PermissionDTO();
                            BeanUtils.copyProperties(r, dto);
                            return dto;
                        })
                        .collect(toList())
        );
        return response;
    }
}
