package com.datapath.checklistukraineapp.service;

import com.datapath.checklistukraineapp.dao.domain.ChecklistDomain;
import com.datapath.checklistukraineapp.dao.domain.ControlEventDomain;
import com.datapath.checklistukraineapp.dao.entity.*;
import com.datapath.checklistukraineapp.dao.entity.classifier.ControlStatusClassifier;
import com.datapath.checklistukraineapp.dao.entity.classifier.ControlTypeClassifier;
import com.datapath.checklistukraineapp.dao.relatioship.TemplateQuestionRelationship;
import com.datapath.checklistukraineapp.dao.service.*;
import com.datapath.checklistukraineapp.dto.ChecklistAnswerDTO;
import com.datapath.checklistukraineapp.dto.ChecklistDTO;
import com.datapath.checklistukraineapp.dto.ControlEventDTO;
import com.datapath.checklistukraineapp.dto.request.event.ChecklistStatusRequest;
import com.datapath.checklistukraineapp.dto.request.event.CreateControlEventRequest;
import com.datapath.checklistukraineapp.dto.request.event.EventTemplateOperationRequest;
import com.datapath.checklistukraineapp.dto.request.event.SaveChecklistRequest;
import com.datapath.checklistukraineapp.dto.response.checklist.ChecklistPageResponse;
import com.datapath.checklistukraineapp.dto.response.checklist.ChecklistResponse;
import com.datapath.checklistukraineapp.exception.EntityNotFoundException;
import com.datapath.checklistukraineapp.exception.PermissionException;
import com.datapath.checklistukraineapp.util.DtoEntityConverter;
import com.datapath.checklistukraineapp.util.UserUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.datapath.checklistukraineapp.util.Constants.*;
import static java.util.Objects.isNull;
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
    private final FinancingTypeDaoService financingTypeService;
    private final FinancingDirectionDaoService financingDirectionService;

    public List<ControlEventDTO> list() {
        List<ControlEventDomain> events;

        if (UserUtils.hasRole(METHODOLOGIST_ROLE)) {
            events = controlEventService.findAll();
        } else {
            events = controlEventService.findAllByUser(UserUtils.getCurrentUserId());
        }

        return events
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
        ControlTypeClassifier type = controlTypeService.findById(request.getControlTypeId());
        ControlStatusClassifier status = controlStatusService.findById(IN_PROCESS_STATUS);
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

        return DtoEntityConverter.mapEntityToDto(controlEventService.save(event));
    }

    public ControlEventDTO get(Long id) {
        return DtoEntityConverter.mapEntityToDto(controlEventService.findById(id));
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

        return DtoEntityConverter.mapEntityToDto(controlEventService.findById(id));
    }

    @Transactional
    public ControlEventDTO addTemplate(EventTemplateOperationRequest request) {
        checkPermission(request.getId());

        customQueryService.createRelationship(
                "ControlEvent", request.getId(), "Template", request.getTemplateId(), "HAS_TEMPLATE"
        );

        return DtoEntityConverter.mapEntityToDto(controlEventService.findById(request.getId()));
    }


    private void checkPermission(Long id) {
        Long currentUserId = UserUtils.getCurrentUserId();

        Set<Long> membersIds = controlEventService.findRelatedUsers(id);

        if (!membersIds.contains(currentUserId) && !UserUtils.hasRole(METHODOLOGIST_ROLE))
            throw new PermissionException("You can't modify this control event");
    }

    public ChecklistPageResponse getChecklists(Long eventId, int page, int size) {
        ChecklistPageResponse dto = new ChecklistPageResponse();

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
    public ChecklistResponse saveChecklist(SaveChecklistRequest request) {
        checkPermission(request.getEventId());

        ControlEventEntity event = controlEventService.findById(request.getEventId());

        ChecklistEntity entity = new ChecklistEntity();
        BeanUtils.copyProperties(request, entity);

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

            if (nonNull(answer.getAmountCharacteristic())) {
                AmountCharacteristicEntity amountCharacteristic = new AmountCharacteristicEntity();
                amountCharacteristic.setTotalAmount(answer.getAmountCharacteristic().getTotalValue());

                answer.getAmountCharacteristic().getTypeAmounts().forEach(a -> {
                    FinancingTypeAmountEntity financingTypeAmount = new FinancingTypeAmountEntity();
                    financingTypeAmount.setAmount(a.getAmount());
                    financingTypeAmount.setType(financingTypeService.findById(a.getFinancingTypeId()));
                    amountCharacteristic.getTypeAmounts().add(financingTypeAmount);
                });

                answer.getAmountCharacteristic().getDirectionAmounts().forEach(a -> {
                    FinancingDirectionAmountEntity financingDirectionAmount = new FinancingDirectionAmountEntity();
                    financingDirectionAmount.setAmount(a.getAmount());
                    financingDirectionAmount.setDirection(financingDirectionService.findById(a.getFinancingDirectionId()));
                    amountCharacteristic.getDirectionAmounts().add(financingDirectionAmount);
                });
                answerEntity.setAmountCharacteristic(amountCharacteristic);
            }

            if (nonNull(answer.getViolationAmountCharacteristic())) {
                ViolationAmountCharacteristicEntity violationAmountCharacteristic = new ViolationAmountCharacteristicEntity();
                BeanUtils.copyProperties(answer.getViolationAmountCharacteristic(), violationAmountCharacteristic);

                answer.getViolationAmountCharacteristic().getDirectionAmounts().forEach(a -> {
                    FinancingDirectionAmountEntity financingDirectionAmount = new FinancingDirectionAmountEntity();
                    financingDirectionAmount.setAmount(a.getAmount());
                    financingDirectionAmount.setDirection(financingDirectionService.findById(a.getFinancingDirectionId()));
                    violationAmountCharacteristic.getDirectionAmounts().add(financingDirectionAmount);
                });
                answerEntity.setViolationAmountCharacteristic(violationAmountCharacteristic);
            }

            entity.getAnswers().add(answerEntity);
        }

        entity = checklistService.save(entity);

        customQueryService.createRelationship(
                "ChecklistResponse", entity.getId(), "Template", template.getId(), "TEMPLATED_BY"
        );

        if (isNull(request.getId())) {
            customQueryService.createRelationship(
                    "ControlEvent", event.getId(), "ChecklistResponse", entity.getId(), "HAS_CHECKLIST"
            );
        }

        return DtoEntityConverter.mapEntityToFullResponse(checklistService.findById(entity.getId()));
    }

    public ChecklistResponse getChecklist(Long id) {
        return DtoEntityConverter.mapEntityToFullResponse(checklistService.findById(id));
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

        return DtoEntityConverter.mapEntityToDto(event);
    }

    @Transactional
    public ChecklistResponse changeStatus(ChecklistStatusRequest request) {
        ChecklistEntity entity = checklistService.findById(request.getId());

        if (IN_PROCESS_STATUS.equals(request.getChecklistStatusId())) {
            entity.setReviewer(null);
        } else if (IN_COMPLETED_STATUS.equals(request.getChecklistStatusId())) {
            entity.setReviewer(userService.findById(UserUtils.getCurrentUserId()));
        }
        entity.setStatus(checklistStatusService.findById(request.getChecklistStatusId()));

        return DtoEntityConverter.mapEntityToFullResponse(checklistService.save(entity));
    }
}
