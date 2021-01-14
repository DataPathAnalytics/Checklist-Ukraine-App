package com.datapath.checklistukraineapp.service;

import com.datapath.checklistukraineapp.dao.domain.ChecklistDomain;
import com.datapath.checklistukraineapp.dao.domain.ControlEventDomain;
import com.datapath.checklistukraineapp.dao.domain.TemplateDomain;
import com.datapath.checklistukraineapp.dao.entity.ChecklistEntity;
import com.datapath.checklistukraineapp.dao.entity.ControlEventEntity;
import com.datapath.checklistukraineapp.dao.entity.ControlObjectEntity;
import com.datapath.checklistukraineapp.dao.entity.UserEntity;
import com.datapath.checklistukraineapp.dao.entity.classifier.ControlStatusEntity;
import com.datapath.checklistukraineapp.dao.entity.classifier.ControlTypeEntity;
import com.datapath.checklistukraineapp.dao.service.*;
import com.datapath.checklistukraineapp.dto.ChecklistDTO;
import com.datapath.checklistukraineapp.dto.ChecklistPageDTO;
import com.datapath.checklistukraineapp.dto.ControlEventDTO;
import com.datapath.checklistukraineapp.dto.TemplateDTO;
import com.datapath.checklistukraineapp.dto.request.event.AddTemplateRequest;
import com.datapath.checklistukraineapp.dto.request.event.CreateChecklistRequest;
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
    private final ChecklistDaoService checklistService;

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

        UserEntity author = userService.findById(currentUserId);

        UserEntity teamLead;
        if (currentUserId.equals(request.getTeamLeadId())) {
            teamLead = author;
        } else {
            teamLead = userService.findById(request.getTeamLeadId());
            request.getMemberIds().add(currentUserId);
        }

        ControlEventEntity event = new ControlEventEntity();
        event.setName(request.getName());
        event.setStatus(status);

        event.setTeamLead(teamLead);
        event.setAuthor(author);
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
        dto.setChecklists(getChecklists(id, 0, 5));
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
        controlEventService.createRelationshipWithTemplate(request.getId(), Collections.singleton(request.getTemplateId()));
        return get(request.getId());
    }

    private ControlEventDTO mapEntityToDto(ControlEventDomain event, List<TemplateDomain> templates) {
        ControlEventDTO dto = new ControlEventDTO();

        BeanUtils.copyProperties(event, dto);

        dto.setTemplates(
                templates.stream()
                        .map(t -> {
                            TemplateDTO templateDto = new TemplateDTO();
                            BeanUtils.copyProperties(t, templateDto);
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

    public ChecklistPageDTO getChecklists(Long eventId, int page, int size) {
        ChecklistPageDTO dto = new ChecklistPageDTO();

        List<ChecklistDomain> checklists = checklistService.findEventChecklists(eventId);

        dto.setTotalCount(checklists.size());
        dto.setTotalPageCount((int) Math.ceil((double) checklists.size() / size));
        dto.setCurrentPage(page);
        dto.setPageSize(size);
        dto.setChecklists(
                checklists.stream()
                        .skip(page * size)
                        .limit(size)
                        .map(c -> {
                            ChecklistDTO checklistDTO = new ChecklistDTO();
                            BeanUtils.copyProperties(c, checklistDTO);
                            return checklistDTO;
                        }).collect(toList())
        );

        return dto;
    }

    @Transactional
    public ControlEventDTO createChecklist(CreateChecklistRequest request) {
        checkPermission(request.getEventId());

        ChecklistEntity entity = new ChecklistEntity();
        entity.setName(request.getName());

        Long currentUserId = UserUtils.getCurrentUserId();
        UserEntity author = userService.findById(currentUserId);

        UserEntity reviewer;
        if (currentUserId.equals(request.getReviewerId())) {
            reviewer = author;
        } else {
            reviewer = userService.findById(request.getReviewerId());
        }

        entity.setAuthor(author);
        entity.setReviewer(reviewer);

        Long checklistId = checklistService.save(entity);

        controlEventService.createRelationshipWithChecklist(request.getEventId(), checklistId);
        checklistService.createRelationshipWithTemplate(checklistId, request.getTemplateId());

        return get(request.getEventId());
    }

    @Transactional
    public ChecklistDTO getChecklist(Long id) {
        ChecklistDTO dto = new ChecklistDTO();
        ChecklistDomain entity = checklistService.findById(id);
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
}
