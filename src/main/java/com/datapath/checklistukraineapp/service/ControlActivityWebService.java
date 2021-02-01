package com.datapath.checklistukraineapp.service;

import com.datapath.checklistukraineapp.dao.domain.ControlActivityDomain;
import com.datapath.checklistukraineapp.dao.domain.ResponseSessionDomain;
import com.datapath.checklistukraineapp.dao.entity.*;
import com.datapath.checklistukraineapp.dao.service.*;
import com.datapath.checklistukraineapp.dao.service.classifier.ActivityStatusDaoService;
import com.datapath.checklistukraineapp.dao.service.classifier.AnswerTypeDaoService;
import com.datapath.checklistukraineapp.dao.service.classifier.AuthorityDaoService;
import com.datapath.checklistukraineapp.dao.service.classifier.SessionStatusDaoService;
import com.datapath.checklistukraineapp.dto.AnswerDTO;
import com.datapath.checklistukraineapp.dto.ControlActivityDTO;
import com.datapath.checklistukraineapp.dto.ResponseSessionDTO;
import com.datapath.checklistukraineapp.dto.SessionPageDTO;
import com.datapath.checklistukraineapp.dto.request.activity.CreateControlActivityRequest;
import com.datapath.checklistukraineapp.dto.request.activity.ResponseSessionStatusRequest;
import com.datapath.checklistukraineapp.dto.request.activity.SaveResponseSessionRequest;
import com.datapath.checklistukraineapp.dto.request.activity.TemplateOperationRequest;
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
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static com.datapath.checklistukraineapp.util.Constants.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.*;
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
    private final ResponseSessionDaoService responseSessionService;
    private final SessionStatusDaoService sessionStatusService;
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

            QuestionExecutionEntity questionExecution = config.getQuestionExecutions().stream()
                    .filter(q -> a.getQuestionId().equals(q.getId()))
                    .findFirst().orElseThrow(() -> new EntityNotFoundException("questionExecution", a.getQuestionId()));

            answerEntity.setParentFeatureId(questionExecution.getParentFeatureId());
            answerEntity.setQuestionExecution(questionExecution);
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
                Node.ControlActivity.name(), id, Node.ActivityStatus.name(), IN_COMPLETED_STATUS, Relationship.IN_STATUS.name()
        );
        customQueryService.createRelationship(
                Node.ControlActivity.name(), id, Node.ActivityStatus.name(), IN_COMPLETED_STATUS, Relationship.IN_STATUS.name()
        );

        return DtoEntityConverter.map(controlActivityService.findById(id));
    }

    @Transactional
    public ControlActivityDTO addTemplate(TemplateOperationRequest request) {
        checkPermission(request.getId());

        customQueryService.createRelationship(
                Node.ControlActivity.name(), request.getId(), Node.Template.name(), request.getTemplateId(), Relationship.HAS_TEMPLATE.name()
        );

        return DtoEntityConverter.map(controlActivityService.findById(request.getId()));
    }

    private void checkPermission(Long id) {
        Long currentUserId = UserUtils.getCurrentUserId();

        Set<Long> membersIds = controlActivityService.findRelatedUsers(id);

        if (!membersIds.contains(currentUserId) && !UserUtils.hasRole(METHODOLOGIST_ROLE))
            throw new PermissionException("You can't modify this control event");
    }

    public SessionPageDTO getSessions(Long eventId, int page, int size) {
        SessionPageDTO dto = new SessionPageDTO();

        List<ResponseSessionDomain> checklists = responseSessionService.findResponseSessionByActivityId(eventId);

        dto.setTotalCount(checklists.size());
        dto.setTotalPageCount((int) Math.ceil((double) checklists.size() / size));
        dto.setCurrentPage(page);
        dto.setPageSize(size);
        dto.setSessions(
                checklists.stream()
                        .skip(page * size)
                        .limit(size)
                        .map(c -> {
                            ResponseSessionDTO responseSessionDTO = new ResponseSessionDTO();
                            BeanUtils.copyProperties(c, responseSessionDTO);
                            return responseSessionDTO;
                        }).collect(toList())
        );

        return dto;
    }

    @Transactional
    public void saveSession(SaveResponseSessionRequest request) {
        checkPermission(request.getControlActivityId());

        TemplateEntity template = templateService.findById(request.getTemplateId());

        ResponseSessionEntity entity = new ResponseSessionEntity();
        entity.setId(request.getId());
        entity.setName(request.getName());
        entity.setStatus(sessionStatusService.findById(IN_PROCESS_STATUS));

        Map<Long, QuestionExecutionEntity> questionExecutionIdMap = template.getGroups()
                .stream()
                .flatMap(qg -> qg.getQuestions().stream())
                .collect(toMap(QuestionExecutionEntity::getId, Function.identity()));

        for (AnswerDTO answer : request.getAnswers()) {
            AnswerEntity answerEntity = new AnswerEntity();

            answerEntity.setComment(answer.getComment());
            answerEntity.setValues(answer.getValues());

            if (nonNull(answer.getAnswerTypeId())) {
                answerEntity.setAnswerType(answerService.findById(answer.getAnswerTypeId()));
            }

            QuestionExecutionEntity questionExecution = questionExecutionIdMap.get(answer.getQuestionId());
            if (isNull(questionExecution))
                throw new EntityNotFoundException("questionExecution", answer.getQuestionId());

            answerEntity.setQuestionExecution(questionExecution);
            answerEntity.setParentFeatureId(questionExecution.getParentFeatureId());

            entity.getAnswers().add(answerEntity);
        }

        entity = responseSessionService.save(entity);

        customQueryService.createRelationship(
                Node.ResponseSession.name(), entity.getId(), Node.Template.name(), template.getId(), Relationship.TEMPLATED_BY.name()
        );

        customQueryService.createRelationship(
                Node.ControlActivity.name(), request.getControlActivityId(), Node.ResponseSession.name(), entity.getId(), Relationship.HAS_SESSION_RESPONSE.name()
        );

        customQueryService.createRelationship(
                Node.ResponseSession.name(), entity.getId(), Node.User.name(), UserUtils.getCurrentUserId(), Relationship.HAS_AUTHOR.name()
        );
    }

    public ResponseSessionDTO getSession(Long id) {
        return DtoEntityConverter.map(responseSessionService.findById(id));
    }

    @Transactional
    public ControlActivityDTO deleteTemplate(TemplateOperationRequest request) {
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
    public void changeStatus(ResponseSessionStatusRequest request) {
        ResponseSessionEntity entity = responseSessionService.findById(request.getId());

        if (IN_PROCESS_STATUS.equals(request.getChecklistStatusId())) {
            entity.setReviewer(null);
        } else if (IN_COMPLETED_STATUS.equals(request.getChecklistStatusId())) {
            entity.setReviewer(userService.findById(UserUtils.getCurrentUserId()));
        }
        entity.setStatus(sessionStatusService.findById(request.getChecklistStatusId()));
    }
}
