package com.datapath.checklistukraineapp.service;

import com.datapath.checklistukraineapp.dao.domain.ChecklistDomain;
import com.datapath.checklistukraineapp.dao.entity.*;
import com.datapath.checklistukraineapp.dao.entity.classifier.ControlStatusEntity;
import com.datapath.checklistukraineapp.dao.entity.classifier.ControlTypeEntity;
import com.datapath.checklistukraineapp.dao.relatioship.TemplateQuestionRelationship;
import com.datapath.checklistukraineapp.dao.service.*;
import com.datapath.checklistukraineapp.dto.*;
import com.datapath.checklistukraineapp.dto.request.event.ChecklistStatusRequest;
import com.datapath.checklistukraineapp.dto.request.event.CreateControlEventRequest;
import com.datapath.checklistukraineapp.dto.request.event.EventTemplateOperationRequest;
import com.datapath.checklistukraineapp.dto.request.event.SaveChecklistRequest;
import com.datapath.checklistukraineapp.exception.EntityNotFoundException;
import com.datapath.checklistukraineapp.exception.PermissionException;
import com.datapath.checklistukraineapp.util.UserUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.datapath.checklistukraineapp.util.Constants.*;
import static java.util.Objects.nonNull;
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
    private final ChecklistStatusDaoService checklistStatusService;
    private final AnswerDaoService answerService;
    private final CustomQueryDaoService customQueryService;

    public List<ControlEventDTO> list() {
        return controlEventService.findAllByUser(UserUtils.getCurrentUserId())
                .stream()
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
        ControlStatusEntity status = controlStatusService.findById(IN_PROCESS_STATUS);
        UserEntity author = userService.findById(currentUserId);

        ControlEventEntity event = new ControlEventEntity();
        event.setName(request.getName());
        event.setStatus(status);

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

    public ControlEventDTO get(Long id) {
        return mapEntityToDto(controlEventService.findById(id));
    }

    @Transactional
    public ControlEventDTO complete(Long id) {
        checkPermission(id);

        customQueryService.deleteRelationship(
                "ControlEvent", id, "ControlStatus", IN_COMPLETED_STATUS, "IN_STATUS"
        );
        customQueryService.createRelationship(
                "ControlEvent", id, "ControlStatus", IN_COMPLETED_STATUS, "IN_STATUS"
        );

        return mapEntityToDto(controlEventService.findById(id));
    }

    @Transactional
    public ControlEventDTO addTemplate(EventTemplateOperationRequest request) {
        checkPermission(request.getId());

        customQueryService.createRelationship(
                "ControlEvent", request.getId(), "Template", request.getTemplateId(), "HAS_TEMPLATE"
        );

        return mapEntityToDto(controlEventService.findById(request.getId()));
    }

    private ControlEventDTO mapEntityToDto(ControlEventEntity event) {
        ControlEventDTO dto = new ControlEventDTO();

        BeanUtils.copyProperties(event, dto);
        dto.setMembers(
                event.getMembers().stream()
                        .map(UserEntity::getId)
                        .collect(toSet())
        );
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
                        .map(this::mapEntityToDto)
                        .collect(toList())
        );

        dto.setChecklists(checklistPage);

        return dto;
    }

    private void checkPermission(Long id) {
        Long currentUserId = UserUtils.getCurrentUserId();

        Set<Long> membersIds = controlEventService.findRelatedUsers(id);

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
    public ChecklistDTO saveChecklist(SaveChecklistRequest request) {
        checkPermission(request.getEventId());

        ControlEventEntity event = controlEventService.findById(request.getEventId());

        ChecklistEntity entity = new ChecklistEntity();
        entity.setId(request.getId());
        entity.setName(request.getName());

        entity.setAuthor(userService.findById(UserUtils.getCurrentUserId()));
        entity.setStatus(checklistStatusService.findById(IN_PROCESS_STATUS));

        TemplateEntity template = event.getTemplates().stream()
                .filter(t -> request.getTemplateId().equals(t.getId()))
                .findFirst().orElseThrow(() -> new EntityNotFoundException("template", request.getTemplateId()));

        for (ChecklistAnswerDTO answer : request.getAnswers()) {
            ChecklistAnswerEntity answerEntity = new ChecklistAnswerEntity();

            answerEntity.setComment(answer.getComment());
            answerEntity.setViolationAmount(answer.getViolationAmount());

            answerEntity.setAnswer(answerService.findById(answer.getAnswerId()));

            if (nonNull(answer.getQuestionId())) {
                answerEntity.setQuestion(
                        template.getQuestions().stream()
                                .map(TemplateQuestionRelationship::getQuestion)
                                .filter(q -> q.getId().equals(answer.getQuestionId()))
                                .findFirst().orElseThrow(() -> new EntityNotFoundException("question", answer.getQuestionId()))
                );
            }

            entity.getAnswers().add(answerEntity);
        }

        entity = checklistService.save(entity);

        customQueryService.createRelationship(
                "ChecklistResponse", entity.getId(), "Template", template.getId(), "TEMPLATED_BY"
        );

        customQueryService.createRelationship(
                "ControlEvent", event.getId(), "ChecklistResponse", entity.getId(), "HAS_CHECKLIST"
        );

        return mapEntityToDto(checklistService.findById(entity.getId()));
    }

    public ChecklistDTO getChecklist(Long id) {
        return mapEntityToDto(checklistService.findById(id));
    }

    @Transactional
    public ControlEventDTO deleteTemplate(EventTemplateOperationRequest request) {
        checkPermission(request.getId());

        ControlEventEntity event = controlEventService.findById(request.getId());

        boolean existsChecklist = event.getChecklists().stream()
                .anyMatch(c -> request.getTemplateId().equals(c.getTemplate().getId()));

        if (!existsChecklist) {
            event.setTemplates(
                    event.getTemplates().stream()
                            .filter(t -> !request.getTemplateId().equals(t.getId()))
                            .collect(toSet())
            );
        }

        customQueryService.deleteRelationship(
                "ControlEvent", request.getId(), "Template", request.getTemplateId(), "HAS_TEMPLATE"
        );

        return mapEntityToDto(event);
    }

    @Transactional
    public ChecklistDTO changeStatus(ChecklistStatusRequest request) {
        ChecklistEntity entity = checklistService.findById(request.getId());

        if (IN_PROCESS_STATUS.equals(request.getChecklistStatusId())) {
            entity.setReviewer(null);
        } else if (IN_COMPLETED_STATUS.equals(request.getChecklistStatusId())) {
            entity.setReviewer(userService.findById(UserUtils.getCurrentUserId()));
        }
        entity.setStatus(checklistStatusService.findById(request.getChecklistStatusId()));

        return mapEntityToDto(checklistService.save(entity));
    }

    private ChecklistDTO mapEntityToDto(ChecklistEntity entity) {
        ChecklistDTO dto = new ChecklistDTO();

        BeanUtils.copyProperties(entity, dto);
        dto.setAuthorId(entity.getAuthor().getId());
        dto.setTemplateId(entity.getTemplate().getId());
        dto.setTemplateName(entity.getTemplate().getName());
        dto.setChecklistStatusId(entity.getStatus().getChecklistStatusId());

        if (nonNull(entity.getReviewer())) {
            dto.setReviewerId(entity.getReviewer().getId());
        }

        return dto;
    }
}
