package com.datapath.checklistukraineapp.service;

import com.datapath.checklistukraineapp.dao.domain.ControlEventDomain;
import com.datapath.checklistukraineapp.dao.domain.TemplateDomain;
import com.datapath.checklistukraineapp.dao.entity.ControlEventEntity;
import com.datapath.checklistukraineapp.dao.entity.ControlObjectEntity;
import com.datapath.checklistukraineapp.dao.entity.UserEntity;
import com.datapath.checklistukraineapp.dao.entity.classifier.ControlStatusEntity;
import com.datapath.checklistukraineapp.dao.entity.classifier.ControlTypeEntity;
import com.datapath.checklistukraineapp.dao.service.*;
import com.datapath.checklistukraineapp.dto.ControlEventDTO;
import com.datapath.checklistukraineapp.dto.TemplateDTO;
import com.datapath.checklistukraineapp.dto.request.event.AddTemplateRequest;
import com.datapath.checklistukraineapp.dto.request.event.CreateControlEventRequest;
import com.datapath.checklistukraineapp.exception.PermissionException;
import com.datapath.checklistukraineapp.util.UserUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.datapath.checklistukraineapp.util.Constants.COMPLETED_CONTROL_STATUS;
import static com.datapath.checklistukraineapp.util.Constants.IN_PROCESS_CONTROL_STATUS;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@AllArgsConstructor
public class ControlEventWebService {

    private final ControlEventDaoService controlEventService;
    private final UserDaoService userService;
    private final ControlObjectDaoService controlObjectService;
    private final ControlTypeDaoService controlTypeService;
    private final ControlStatusDaoService controlStatusService;
    private final TemplateDaoService templateService;

    public List<ControlEventDTO> list() {
        return controlEventService.findAll().stream()
                .map(c -> {
                    ControlEventDTO dto = new ControlEventDTO();
                    BeanUtils.copyProperties(c, dto);
                    return dto;
                }).collect(toList());
    }

    @Transactional
    public ControlEventDTO create(CreateControlEventRequest request) {
        Long currentUserId = UserUtils.getCurrentUserId();
        ControlObjectEntity object = controlObjectService.findById(request.getControlObjectId());
        ControlTypeEntity type = controlTypeService.findById(request.getControlTypeId());
        ControlStatusEntity status = controlStatusService.findById(IN_PROCESS_CONTROL_STATUS);

        UserEntity user = userService.findById(currentUserId);

        UserEntity teamLead;
        if (!currentUserId.equals(request.getTeamLeadId())) {
            teamLead = userService.findById(request.getTeamLeadId());
        } else {
            teamLead = user;
        }

        ControlEventEntity event = new ControlEventEntity();
        event.setName(request.getName());
        event.setStatus(status);

        event.setTeamLead(teamLead);
        event.setAuthor(user);
        event.setObject(object);
        event.setType(type);

        Long savedId = controlEventService.save(event);

        if (!isEmpty(request.getMemberIds())) {
            controlEventService.createRelationshipWithUser(savedId, request.getMemberIds());
        }

        if (!isEmpty(request.getTemplateIds())) {
            controlEventService.createRelationshipWithTemplate(savedId, request.getTemplateIds());
        }

        return get(savedId);
    }

    @Transactional
    public ControlEventDTO get(Long id) {
        ControlEventDomain event = controlEventService.findById(id);
        List<TemplateDomain> templates = templateService.findByControlEvent(id);
        Set<Long> membersIds = controlEventService.getMembers(id);
        ControlEventDTO dto = mapEntityToDto(event, templates);
        dto.setMembers(membersIds);
        return dto;
    }

    @Transactional
    public ControlEventDTO complete(Long id) {
        checkPermission(id);
        controlEventService.removeRelationshipWithStatus(id);
        controlEventService.createRelationshipWithStatus(id, COMPLETED_CONTROL_STATUS);
        return get(id);
    }

    @Transactional
    public ControlEventDTO addTemplate(AddTemplateRequest request) {
        checkPermission(request.getId());
        controlEventService.createRelationshipWithTemplate(request.getId(), Collections.singletonList(request.getTemplateId()));
        return get(request.getId());
    }

    private ControlEventDTO mapEntityToDto(ControlEventDomain event, List<TemplateDomain> templates) {
        ControlEventDTO dto = new ControlEventDTO();

        BeanUtils.copyProperties(event, dto);

        dto.setTemplates(
                templates.stream()
                        .map(t -> {
                            TemplateDTO templateDto = new TemplateDTO();
                            BeanUtils.copyProperties(t, dto);
                            return templateDto;
                        })
                        .collect(toList())
        );

        return dto;
    }

    private void checkPermission(Long eventId) {
        Long currentUserId = UserUtils.getCurrentUserId();

        Set<Long> membersIds = controlEventService.findLinkedUsers(eventId);

        if (!membersIds.contains(currentUserId))
            throw new PermissionException("You can't modify this control event");
    }
}
