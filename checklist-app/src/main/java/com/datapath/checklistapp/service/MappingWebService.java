package com.datapath.checklistapp.service;

import com.datapath.checklistapp.dao.service.DepartmentDaoService;
import com.datapath.checklistapp.dao.service.KnowledgeCategoryDaoService;
import com.datapath.checklistapp.dao.service.classifier.*;
import com.datapath.checklistapp.dto.DepartmentDTO;
import com.datapath.checklistapp.dto.IdValueDTO;
import com.datapath.checklistapp.dto.response.mapping.MappingPrivateResponse;
import com.datapath.checklistapp.dto.response.mapping.MappingPublicResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static com.datapath.checklistapp.util.Constants.ADMIN_ROLE;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class MappingWebService {

    private final DepartmentDaoService departmentDaoService;
    private final PermissionDaoService permissionDaoService;
    private final AnswerTypeDaoService answerTypeService;
    private final ActivityStatusDaoService activityStatusService;
    private final SessionStatusDaoService sessionStatusService;
    private final KnowledgeCategoryDaoService knowledgeCategoryService;
    private final TemplateTypeDaoService templateTypeService;
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
                        .filter(r -> !ADMIN_ROLE.equals(r.getRole()))
                        .map(p -> new IdValueDTO(p.getPermissionId(), p.getValue()))
                        .collect(toList())
        );
        response.setAnswerTypes(
                answerTypeService.findAll().stream()
                        .map(a -> new IdValueDTO(a.getAnswerTypeId(), a.getValue())).collect(toList())
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
        response.setKnowledgeCategories(
                knowledgeCategoryService.findAll().stream()
                        .map(k -> new IdValueDTO(k.getId(), k.getName()))
                        .collect(toList())
        );
        response.setTemplateTypes(
                templateTypeService.findAll().stream()
                        .map(e -> new IdValueDTO(e.getTemplateTypeId(), e.getValue()))
                        .collect(toList())
        );
        response.setQuestionTypes(
                questionTypeService.findAll().stream()
                        .map(e -> new IdValueDTO(e.getQuestionTypeId(), e.getValue()))
                        .collect(toList())
        );
        return response;
    }
}