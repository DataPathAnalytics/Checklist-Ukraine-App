package com.datapath.checklistapp.service;

import com.datapath.checklistapp.dao.service.DepartmentDaoService;
import com.datapath.checklistapp.dao.service.classifier.*;
import com.datapath.checklistapp.dto.DepartmentDTO;
import com.datapath.checklistapp.dto.IdValueDTO;
import com.datapath.checklistapp.dto.PermissionDTO;
import com.datapath.checklistapp.dto.response.mapping.MappingPrivateResponse;
import com.datapath.checklistapp.dto.response.mapping.MappingPublicResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class MappingWebService {

    private final DepartmentDaoService departmentDaoService;
    private final PermissionDaoService permissionDaoService;
    private final ActivityStatusDaoService activityStatusService;
    private final SessionStatusDaoService sessionStatusService;
    private final TemplateConfigTypeDaoService templateTypeService;
    private final QuestionTypeDaoService questionTypeService;

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
                        .map(p -> new PermissionDTO(p.getPermissionId(), p.getRole(), p.getValue()))
                        .collect(toList())
        );
        response.setSessionStatuses(
                sessionStatusService.findAll().stream()
                        .map(e -> new IdValueDTO(e.getSessionStatusId(), e.getValue()))
                        .collect(toList())
        );
        response.setActivityStatuses(
                activityStatusService.findAll().stream()
                        .map(s -> new IdValueDTO(s.getActivityStatusId(), s.getValue()))
                        .collect(toList())
        );
        response.setTemplateConfigTypes(
                templateTypeService.findAll().stream()
                        .map(e -> new IdValueDTO(e.getTypeId(), e.getValue()))
                        .collect(toList())
        );
        response.setQuestionTypes(
                questionTypeService.findAll().stream()
                        .map(e -> new IdValueDTO(e.getTypeId(), e.getValue()))
                        .collect(toList())
        );
        return response;
    }
}
