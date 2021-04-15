package com.datapath.checklistapp.service;

import com.datapath.checklistapp.dao.service.classifier.ActivityStatusDaoService;
import com.datapath.checklistapp.dao.service.classifier.PermissionDaoService;
import com.datapath.checklistapp.dao.service.classifier.SessionStatusDaoService;
import com.datapath.checklistapp.dao.service.classifier.TemplateConfigTypeDaoService;
import com.datapath.checklistapp.dto.IdValueDTO;
import com.datapath.checklistapp.dto.PermissionDTO;
import com.datapath.checklistapp.dto.response.mapping.MappingPrivateResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class MappingWebService {

    private final PermissionDaoService permissionDaoService;
    private final ActivityStatusDaoService activityStatusService;
    private final SessionStatusDaoService sessionStatusService;
    private final TemplateConfigTypeDaoService templateTypeService;

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
        return response;
    }
}
