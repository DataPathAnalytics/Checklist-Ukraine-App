package com.datapath.checklistukraineapp.service;

import com.datapath.checklistukraineapp.dao.domain.ControlActivityDomain;
import com.datapath.checklistukraineapp.dao.domain.ResponseSessionDomain;
import com.datapath.checklistukraineapp.dao.entity.AnswerEntity;
import com.datapath.checklistukraineapp.dao.entity.ControlActivityEntity;
import com.datapath.checklistukraineapp.dao.entity.ResponseSessionEntity;
import com.datapath.checklistukraineapp.dao.entity.TemplateConfigEntity;
import com.datapath.checklistukraineapp.dao.service.*;
import com.datapath.checklistukraineapp.dao.service.classifier.ActivityStatusDaoService;
import com.datapath.checklistukraineapp.dao.service.classifier.AnswerTypeDaoService;
import com.datapath.checklistukraineapp.dao.service.classifier.AuthorityDaoService;
import com.datapath.checklistukraineapp.dao.service.classifier.SessionStatusDaoService;
import com.datapath.checklistukraineapp.dto.ChecklistDTO;
import com.datapath.checklistukraineapp.dto.ControlActivityDTO;
import com.datapath.checklistukraineapp.dto.request.activity.ChecklistStatusRequest;
import com.datapath.checklistukraineapp.dto.request.activity.CreateControlActivityRequest;
import com.datapath.checklistukraineapp.dto.request.activity.EventTemplateOperationRequest;
import com.datapath.checklistukraineapp.dto.request.activity.SaveChecklistRequest;
import com.datapath.checklistukraineapp.dto.response.checklist.ChecklistPageResponse;
import com.datapath.checklistukraineapp.dto.response.checklist.ChecklistResponse;
import com.datapath.checklistukraineapp.exception.EntityNotFoundException;
import com.datapath.checklistukraineapp.exception.PermissionException;
import com.datapath.checklistukraineapp.util.DtoEntityConverter;
import com.datapath.checklistukraineapp.util.Node;
import com.datapath.checklistukraineapp.util.Relationship;
import com.datapath.checklistukraineapp.util.UserUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static com.datapath.checklistukraineapp.util.Constants.*;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@AllArgsConstructor
public class ControlActivityWebService {

    private final ControlActivityDaoService controlActivityService;
    private final UserDaoService userService;
    private final AuthorityDaoService authorityService;
    private final ActivityStatusDaoService activityStatusService;
    private final TemplateDaoService templateService;
    private final TemplateConfigDaoService templateConfigService;
    private final ResponseSessionDaoService checklistService;
    private final SessionStatusDaoService checklistStatusService;
    private final AnswerTypeDaoService answerService;
    private final CustomQueryDaoService customQueryService;

    public List<ControlActivityDTO> list() {
        List<ControlActivityDomain> activities;

        if (UserUtils.hasRole(METHODOLOGIST_ROLE)) {
            activities = controlActivityService.findAll();
        } else {
            activities = controlActivityService.findAllByUser(UserUtils.getCurrentUserId());
        }

        return activities
                .stream()
                .map(c -> {
                    ControlActivityDTO dto = new ControlActivityDTO();
                    BeanUtils.copyProperties(c, dto);
                    return dto;
                }).collect(toList());
    }

    @Transactional
    public void create(CreateControlActivityRequest request) {
        TemplateConfigEntity config = templateConfigService.findById(request.getTemplateConfigId());

        Long currentUserId = UserUtils.getCurrentUserId();

        ControlActivityEntity activityEntity = new ControlActivityEntity();
        activityEntity.setName(request.getName());
        activityEntity.setStatus(activityStatusService.findById(IN_PROCESS_STATUS));
        activityEntity.setAuthor(userService.findById(currentUserId));
        activityEntity.setAuthority(authorityService.findById(request.getAuthorityId()));

        ResponseSessionEntity activityResponse = new ResponseSessionEntity();
        request.getAnswers().forEach(a -> {
            AnswerEntity answerEntity = new AnswerEntity();
            answerEntity.setComment(a.getComment());
            if (nonNull(a.getAnswerTypeId())) {
                answerEntity.setAnswerType(answerService.findById(a.getAnswerTypeId()));
            }
            answerEntity.setQuestionExecution(
                    config.getQuestionExecutions().stream()
                            .filter(q -> a.getQuestionId().equals(q.getId()))
                            .findFirst().orElseThrow(() -> new EntityNotFoundException("questionExecution", a.getQuestionId()))
            );
            answerEntity.setValues(a.getValues());

            activityResponse.getAnswers().add(answerEntity);
        });
        activityEntity.setActivityResponse(activityResponse);
        ControlActivityEntity savedEntity = controlActivityService.save(activityEntity);

        customQueryService.createRelationship(
                Node.ResponseSession.name(),
                savedEntity.getActivityResponse().getId(),
                Node.TemplateConfig.name(),
                request.getTemplateConfigId(),
                Relationship.TEMPLATED_BY.name()
        );

        if (!isEmpty(request.getMemberIds())) {
            request.getMemberIds().forEach(id -> customQueryService.createRelationship(
                    Node.ControlActivity.name(),
                    savedEntity.getId(),
                    Node.User.name(),
                    id,
                    Relationship.HAS_MEMBER.name()
            ));
        }

        if (!isEmpty(request.getTemplateIds())) {
            request.getTemplateIds().forEach(id -> customQueryService.createRelationship(
                    Node.ControlActivity.name(),
                    savedEntity.getId(),
                    Node.Template.name(),
                    id,
                    Relationship.HAS_TEMPLATE.name()
            ));
        }
    }

    public ControlActivityDTO get(Long id) {
        return DtoEntityConverter.map(controlActivityService.findById(id));
    }

    @Transactional
    public ControlActivityDTO complete(Long id) {
        checkPermission(id);

        customQueryService.deleteRelationship(
                "ControlEvent", id, "ControlStatus", IN_COMPLETED_STATUS, "IN_STATUS"
        );
        customQueryService.createRelationship(
                "ControlEvent", id, "ControlStatus", IN_COMPLETED_STATUS, "IN_STATUS"
        );

        return DtoEntityConverter.map(controlActivityService.findById(id));
    }

    @Transactional
    public ControlActivityDTO addTemplate(EventTemplateOperationRequest request) {
        checkPermission(request.getId());

        customQueryService.createRelationship(
                "ControlEvent", request.getId(), "Template", request.getTemplateId(), "HAS_TEMPLATE"
        );

        return DtoEntityConverter.map(controlActivityService.findById(request.getId()));
    }

    private void checkPermission(Long id) {
        Long currentUserId = UserUtils.getCurrentUserId();

        Set<Long> membersIds = controlActivityService.findRelatedUsers(id);

        if (!membersIds.contains(currentUserId) && !UserUtils.hasRole(METHODOLOGIST_ROLE))
            throw new PermissionException("You can't modify this control event");
    }

    public ChecklistPageResponse getChecklists(Long eventId, int page, int size) {
        ChecklistPageResponse dto = new ChecklistPageResponse();

        List<ResponseSessionDomain> checklists = checklistService.findEventChecklists(eventId);

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

        ControlActivityEntity event = controlActivityService.findById(request.getEventId());

        ResponseSessionEntity entity = new ResponseSessionEntity();
//        BeanUtils.copyProperties(request, entity);
//
//        entity.setAuthor(userService.findById(UserUtils.getCurrentUserId()));
//        entity.setStatus(checklistStatusService.findById(IN_PROCESS_STATUS));
//
//        TemplateEntity template = event.getTemplates().stream()
//                .filter(t -> request.getTemplateId().equals(t.getId()))
//                .findFirst().orElseThrow(() -> new EntityNotFoundException("template", request.getTemplateId()));
//
//        for (ChecklistAnswerDTO answer : request.getAnswers()) {
//            AnswerEntity answerEntity = new AnswerEntity();
//
//            answerEntity.setComment(answer.getComment());
//            answerEntity.setViolationAmount(answer.getViolationAmount());
//
//            answerEntity.setAnswer(answerService.findById(answer.getAnswerId()));
//
//            if (nonNull(answer.getQuestionId())) {
//                answerEntity.setQuestion(
//                        template.getFactQuestions().stream()
//                                .map(TemplateQuestionRelationship::getQuestion)
//                                .filter(q -> q.getId().equals(answer.getQuestionId()))
//                                .findFirst().orElseThrow(() -> new EntityNotFoundException("question", answer.getQuestionId()))
//                );
//            }
//
//            if (nonNull(answer.getAmountCharacteristic())) {
//                AmountCharacteristicEntity amountCharacteristic = new AmountCharacteristicEntity();
//                amountCharacteristic.setTotalAmount(answer.getAmountCharacteristic().getTotalValue());
//
//                answer.getAmountCharacteristic().getTypeAmounts().forEach(a -> {
//                    FinancingTypeAmountEntity financingTypeAmount = new FinancingTypeAmountEntity();
//                    financingTypeAmount.setAmount(a.getAmount());
//                    financingTypeAmount.setType(financingTypeService.findById(a.getFinancingTypeId()));
//                    amountCharacteristic.getTypeAmounts().add(financingTypeAmount);
//                });
//
//                answer.getAmountCharacteristic().getDirectionAmounts().forEach(a -> {
//                    FinancingDirectionAmountEntity financingDirectionAmount = new FinancingDirectionAmountEntity();
//                    financingDirectionAmount.setAmount(a.getAmount());
//                    financingDirectionAmount.setDirection(financingDirectionService.findById(a.getFinancingDirectionId()));
//                    amountCharacteristic.getDirectionAmounts().add(financingDirectionAmount);
//                });
//                answerEntity.setAmountCharacteristic(amountCharacteristic);
//            }
//
//            if (nonNull(answer.getViolationAmountCharacteristic())) {
//                ViolationAmountCharacteristicEntity violationAmountCharacteristic = new ViolationAmountCharacteristicEntity();
//                BeanUtils.copyProperties(answer.getViolationAmountCharacteristic(), violationAmountCharacteristic);
//
//                answer.getViolationAmountCharacteristic().getDirectionAmounts().forEach(a -> {
//                    FinancingDirectionAmountEntity financingDirectionAmount = new FinancingDirectionAmountEntity();
//                    financingDirectionAmount.setAmount(a.getAmount());
//                    financingDirectionAmount.setDirection(financingDirectionService.findById(a.getFinancingDirectionId()));
//                    violationAmountCharacteristic.getDirectionAmounts().add(financingDirectionAmount);
//                });
//                answerEntity.setViolationAmountCharacteristic(violationAmountCharacteristic);
//            }
//
//            entity.getAnswers().add(answerEntity);
//        }
//
//        entity = checklistService.save(entity);
//
//        customQueryService.createRelationship(
//                "ChecklistResponse", entity.getId(), "Template", template.getId(), "TEMPLATED_BY"
//        );
//
//        if (isNull(request.getId())) {
//            customQueryService.createRelationship(
//                    "ControlEvent", event.getId(), "ChecklistResponse", entity.getId(), "HAS_CHECKLIST"
//            );
//        }

        return DtoEntityConverter.mapFullResponse(checklistService.findById(entity.getId()));
    }

    public ChecklistResponse getChecklist(Long id) {
        return DtoEntityConverter.mapFullResponse(checklistService.findById(id));
    }

    @Transactional
    public ControlActivityDTO deleteTemplate(EventTemplateOperationRequest request) {
        checkPermission(request.getId());

        ControlActivityEntity activity = controlActivityService.findById(request.getId());

        boolean existsChecklist = activity.getSessionResponses().stream()
                .anyMatch(c -> request.getTemplateId().equals(c.getTemplate().getId()));

        if (!existsChecklist) {
            activity.setTemplates(
                    activity.getTemplates().stream()
                            .filter(t -> !request.getTemplateId().equals(t.getId()))
                            .collect(toSet())
            );
        }

        customQueryService.deleteRelationship(
                Node.ControlActivity.name(), request.getId(), Node.Template.name(), request.getTemplateId(), Relationship.HAS_TEMPLATE.name()
        );

        return DtoEntityConverter.map(activity);
    }

    @Transactional
    public ChecklistResponse changeStatus(ChecklistStatusRequest request) {
        ResponseSessionEntity entity = checklistService.findById(request.getId());

        if (IN_PROCESS_STATUS.equals(request.getChecklistStatusId())) {
            entity.setReviewer(null);
        } else if (IN_COMPLETED_STATUS.equals(request.getChecklistStatusId())) {
            entity.setReviewer(userService.findById(UserUtils.getCurrentUserId()));
        }
        entity.setStatus(checklistStatusService.findById(request.getChecklistStatusId()));

        return DtoEntityConverter.mapFullResponse(checklistService.save(entity));
    }
}
