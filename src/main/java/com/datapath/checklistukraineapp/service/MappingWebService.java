package com.datapath.checklistukraineapp.service;

import com.datapath.checklistukraineapp.dao.service.*;
import com.datapath.checklistukraineapp.dto.*;
import com.datapath.checklistukraineapp.dto.response.mapping.MappingPrivateResponse;
import com.datapath.checklistukraineapp.dto.response.mapping.MappingPublicResponse;
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
    private final ControlStatusDaoService controlStatusService;
    private final FinancingTypeDaoService financingTypeService;
    private final FinancingDirectionDaoService financingDirectionService;

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
        response.setControlStatuses(
                controlStatusService.findAll().stream()
                        .map(s -> new ControlStatusDTO(s.getControlStatusId(), s.getValue()))
                        .collect(toList())
        );
        response.setFinancingTypes(
                financingTypeService.findAll().stream()
                        .map(ft -> new FinancingTypeDTO(ft.getId(), ft.getName()))
                        .collect(toList())
        );
        response.setFinancingDirections(
                financingDirectionService.findAll().stream()
                        .map(fd -> new FinancingDirectionDTO(fd.getId(), fd.getName()))
                        .collect(toList())
        );
        return response;
    }
}
