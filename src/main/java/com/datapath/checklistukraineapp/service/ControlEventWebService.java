package com.datapath.checklistukraineapp.service;

import com.datapath.checklistukraineapp.dao.domain.ChecklistDomain;
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
import com.datapath.checklistukraineapp.exception.TemplateException;
import com.datapath.checklistukraineapp.util.UserUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import static com.datapath.checklistukraineapp.util.Constants.*;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
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

        if (!isEmpty(request.getTemplateIds())) {
            event.setTemplates(new HashSet<>(templateService.findByIds(request.getTemplateIds())));
        }
        if (!isEmpty(request.getMemberIds())) {
            event.setMembers(new HashSet<>(userService.findByIds(request.getMemberIds())));
        }

        return mapEntityToDto(controlEventService.save(event));
    }

    @Transactional
    public ControlEventDTO get(Long id) {
        return mapEntityToDto(controlEventService.findById(id));
    }

    @Transactional
    public ControlEventDTO complete(Long id) {
        ControlEventEntity event = controlEventService.findById(id);

        checkPermission(event);

        event.setStatus(controlStatusService.findById(COMPLETED_CONTROL_STATUS));
        ControlEventEntity savedEvent = controlEventService.save(event);
        return mapEntityToDto(savedEvent);
    }

    @Transactional
    public ControlEventDTO addTemplate(AddTemplateRequest request) {
        ControlEventEntity event = controlEventService.findById(request.getId());

        checkPermission(event);

        event.getTemplates().add(templateService.findById(request.getTemplateId()));

        return mapEntityToDto(controlEventService.save(event));
    }

    private ControlEventDTO mapEntityToDto(ControlEventEntity event) {
        ControlEventDTO dto = new ControlEventDTO();

        BeanUtils.copyProperties(event, dto);
        dto.setMembers(
                event.getMembers().stream()
                        .map(UserEntity::getId)
                        .collect(toSet())
        );
        dto.setTeamLeadId(event.getTeamLead().getId());
        dto.setAuthorId(event.getAuthor().getId());
        dto.setControlStatusId(event.getStatus().getControlStatusId());
        dto.setControlObjectName(event.getObject().getName());
        dto.setControlObjectId(event.getObject().getControlObjectId());
        dto.setControlTypeId(event.getType().getControlTypeId());
        dto.setTemplates(
                event.getTemplates().stream()
                        .map(t -> {
                            TemplateDTO templateDto = new TemplateDTO();
                            BeanUtils.copyProperties(t, templateDto);
                            templateDto.setAuthorId(t.getAuthor().getId());
                            templateDto.setFolderId(t.getFolder().getId());
                            return templateDto;
                        }).collect(toList()));

        ChecklistPageDTO checklistPage = new ChecklistPageDTO();
        checklistPage.setPageSize(DEFAULT_EVENT_CHECKLIST_COUNT);
        checklistPage.setCurrentPage(DEFAULT_EVENT_CHECKLIST_PAGE);
        checklistPage.setTotalCount(event.getChecklists().size());
        checklistPage.setTotalPageCount((int) Math.ceil((double) event.getChecklists().size() / DEFAULT_EVENT_CHECKLIST_COUNT));
        checklistPage.setChecklists(
                event.getChecklists().stream()
                        .limit(DEFAULT_EVENT_CHECKLIST_COUNT)
                        .sorted(Comparator.comparing(ChecklistEntity::getDateCreated)
                                .thenComparing(ChecklistEntity::getName))
                        .map(c -> {
                            ChecklistDTO checklistDTO = new ChecklistDTO();
                            BeanUtils.copyProperties(c, checklistDTO);
                            checklistDTO.setAuthorId(c.getAuthor().getId());
                            checklistDTO.setReviewerId(c.getReviewer().getId());
                            checklistDTO.setTemplateId(c.getTemplate().getId());
                            checklistDTO.setTemplateName(c.getTemplate().getName());

                            return checklistDTO;
                        }).collect(toList())
        );

        dto.setChecklists(checklistPage);

        return dto;
    }

    private void checkPermission(ControlEventEntity event) {
        Long currentUserId = UserUtils.getCurrentUserId();

        List<Long> membersIds = event.getMembers().stream().map(UserEntity::getId).collect(toList());

        if (!membersIds.contains(currentUserId)
                && !currentUserId.equals(event.getTeamLead().getId())
                && !currentUserId.equals(event.getAuthor().getId()))
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
        ControlEventEntity event = controlEventService.findById(request.getEventId());

        checkPermission(event);

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

        entity.setTemplate(
                event.getTemplates().stream()
                        .filter(t -> request.getTemplateId().equals(t.getId()))
                        .findFirst().orElseThrow(() -> new TemplateException(request.getTemplateId()))
        );

        event.getChecklists().add(entity);

        return mapEntityToDto(controlEventService.save(event));
    }

    @Transactional
    public ChecklistDTO getChecklist(Long id) {
        ChecklistDTO dto = new ChecklistDTO();
        ChecklistEntity entity = checklistService.findById(id);
        BeanUtils.copyProperties(entity, dto);
        dto.setTemplateName(entity.getTemplate().getName());
        dto.setTemplateId(entity.getTemplate().getId());
        dto.setAuthorId(entity.getAuthor().getId());
        dto.setReviewerId(entity.getReviewer().getId());
        return dto;
    }
}
