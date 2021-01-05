package com.datapath.checklistukraineapp.service;

import com.datapath.checklistukraineapp.dao.service.AnswerDaoService;
import com.datapath.checklistukraineapp.dao.service.ControlTypeDaoService;
import com.datapath.checklistukraineapp.dao.service.DepartmentDaoService;
import com.datapath.checklistukraineapp.dao.service.PermissionDaoService;
import com.datapath.checklistukraineapp.dto.AnswerDTO;
import com.datapath.checklistukraineapp.dto.ControlTypeDTO;
import com.datapath.checklistukraineapp.dto.DepartmentDTO;
import com.datapath.checklistukraineapp.dto.PermissionDTO;
import com.datapath.checklistukraineapp.dto.response.MappingPrivateResponse;
import com.datapath.checklistukraineapp.dto.response.MappingPublicResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static com.datapath.checklistukraineapp.util.Constants.ADMIN_ROLE;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class MappingWebService {

    private final DepartmentDaoService departmentDaoService;
    private final PermissionDaoService permissionDaoService;
    private final AnswerDaoService answerService;
    private final ControlTypeDaoService controlTypeService;

    public MappingPublicResponse getPublicMappings() {
        MappingPublicResponse response = new MappingPublicResponse();
        response.setDepartments(
                departmentDaoService.findAll()
                        .stream()
                        .map(d -> new DepartmentDTO(d.getId(), d.getRegion()))
                        .collect(toList())
        );
        return response;
    }

    public MappingPrivateResponse getPrivateMappings() {
        MappingPrivateResponse response = new MappingPrivateResponse();
        response.setPermissions(
                permissionDaoService.findAll().stream()
                        .filter(r -> !ADMIN_ROLE.equals(r.getRole()))
                        .map(p -> new PermissionDTO(p.getPermissionId(), p.getValue()))
                        .collect(toList())
        );
        response.setAnswers(
                answerService.findAll().stream()
                        .map(a -> new AnswerDTO(a.getAnswerId(), a.getValue())).collect(toList())
        );
        response.setControlTypes(
                controlTypeService.findAll().stream()
                        .map(c -> new ControlTypeDTO(c.getControlTypeId(), c.getValue()))
                        .collect(toList())
        );
        return response;
    }
}
