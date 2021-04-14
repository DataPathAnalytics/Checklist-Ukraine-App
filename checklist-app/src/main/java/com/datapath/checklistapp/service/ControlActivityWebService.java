package com.datapath.checklistapp.service;

import com.datapath.checklistapp.dao.domain.ControlActivityDomain;
import com.datapath.checklistapp.dao.domain.ResponseSessionDomain;
import com.datapath.checklistapp.dao.entity.*;
import com.datapath.checklistapp.dao.service.*;
import com.datapath.checklistapp.dao.service.classifier.ActivityStatusDaoService;
import com.datapath.checklistapp.dao.service.classifier.SessionStatusDaoService;
import com.datapath.checklistapp.dto.AnswerDTO;
import com.datapath.checklistapp.dto.ControlActivityDTO;
import com.datapath.checklistapp.dto.ResponseSessionDTO;
import com.datapath.checklistapp.dto.SessionPageDTO;
import com.datapath.checklistapp.dto.request.activity.*;
import com.datapath.checklistapp.exception.EntityNotFoundException;
import com.datapath.checklistapp.exception.PermissionException;
import com.datapath.checklistapp.exception.ValidationException;
import com.datapath.checklistapp.service.converter.structure.AnswerConverter;
import com.datapath.checklistapp.service.converter.structure.ControlActivityConverter;
import com.datapath.checklistapp.service.converter.structure.ResponseSessionConverter;
import com.datapath.checklistapp.util.UserUtils;
import com.datapath.checklistapp.util.database.Field;
import com.datapath.checklistapp.util.database.Node;
import com.datapath.checklistapp.util.database.Relationship;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;

import static com.datapath.checklistapp.util.Constants.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.*;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@AllArgsConstructor
public class ControlActivityWebService {

    private final ControlActivityDaoService controlActivityService;
    private final UserDaoService userService;
    private final ActivityStatusDaoService activityStatusService;
    private final TemplateDaoService templateService;
    private final TemplateConfigDaoService templateConfigService;
    private final ResponseSessionDaoService responseSessionService;
    private final SessionStatusDaoService sessionStatusService;
    private final CustomQueryDaoService customQueryService;

    private final AnswerConverter answerConverter;
    private final ControlActivityConverter controlActivityConverter;
    private final ResponseSessionConverter responseSessionConverter;

    private final ObjectMapper mapper;

    @Transactional
    public List<ControlActivityDTO> list() {
        List<ControlActivityDomain> activities;

        if (UserUtils.hasRole(METHODOLOGIST_ROLE)) {
            activities = controlActivityService.findAll(null);
        } else {
            activities = controlActivityService.findAll(UserUtils.getCurrentUserId());
        }

        return activities
                .stream()
                .sorted(Comparator.comparing(ControlActivityDomain::getDateCreated).reversed())
                .map(c -> {
                    ControlActivityDTO dto = new ControlActivityDTO();
                    BeanUtils.copyProperties(c, dto);

                    dto.setStatusId(c.getStatusId());
                    dto.setAuthorId(c.getAuthorId());

                    ResponseSessionEntity responseSession = responseSessionService.findById(c.getActivityResponseId());

                    QuestionExecutionEntity objectQuestion = responseSession.getTemplateConfig().getObjectQuestion();

                    AnswerEntity objectAnswer = responseSession.getAnswers().stream()
                            .filter(a -> objectQuestion.getId().equals(a.getQuestionExecution().getId()))
                            .findFirst()
                            .orElseThrow(() -> new ValidationException("Not found answer to object question. Activity id " + c.getId()));

                    dto.setAnswers(
                            Collections.singletonList(
                                    answerConverter.mapOnlyFilterFields(
                                            objectAnswer,
                                            objectQuestion.getQuestion().getAnswerStructure().getFields().stream()
                                                    .filter(FieldDescriptionEntity::isTitle)
                                                    .map(FieldDescriptionEntity::getName)
                                                    .collect(toSet())
                                    )
                            )
                    );

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

        ResponseSessionEntity activityResponse = new ResponseSessionEntity();

        Map<Long, QuestionExecutionEntity> questionExecutionIdMap = getQuestionExecutionMap(config);

        request.getAnswers().forEach(a -> {
            QuestionExecutionEntity questionExecution = questionExecutionIdMap.get(a.getQuestionId());

            AnswerEntity answerEntity = new AnswerEntity();
            answerEntity.setComment(a.getComment());

            if (nonNull(a.getValueId())) {
//                answerEntity.setValueId(a.getValueId());
            }

            answerEntity.setQuestionExecution(questionExecution);

            if (!isEmpty(a.getValues())) {
                try {
                    answerEntity.setValues(mapper.writeValueAsString(a.getValues()));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
            activityResponse.getAnswers().add(answerEntity);
        });
        activityEntity.setActivityResponse(activityResponse);
        ControlActivityEntity savedEntity = controlActivityService.save(activityEntity);

        customQueryService.createRelationship(
                Node.ResponseSession.name(), null, savedEntity.getActivityResponse().getId(),
                Node.TemplateConfig.name(), null, request.getTemplateConfigId(),
                Relationship.TEMPLATED_BY.name()
        );

        if (!isEmpty(request.getMemberIds())) {
            request.getMemberIds().forEach(id -> customQueryService.createRelationship(
                    Node.ControlActivity.name(), null, savedEntity.getId(),
                    Node.User.name(), null, id,
                    Relationship.HAS_MEMBER.name()
            ));
        }

        if (!isEmpty(request.getTemplateIds())) {
            request.getTemplateIds().forEach(id -> customQueryService.createRelationship(
                    Node.ControlActivity.name(), null, savedEntity.getId(),
                    Node.Template.name(), null, id,
                    Relationship.HAS_TEMPLATE.name()
            ));
        }
    }

    @Transactional
    public ControlActivityDTO get(Long id) {
        return controlActivityConverter.map(extractControlActivityById(id));
    }

    @Transactional
    public ControlActivityDTO complete(Long id) {
        checkPermission(id);

        customQueryService.deleteRelationship(
                Node.ControlActivity.name(), null, id,
                Node.ActivityStatus.name(), Field.activityStatusId.name(), IN_PROCESS_STATUS,
                Relationship.IN_STATUS.name()
        );
        customQueryService.createRelationship(
                Node.ControlActivity.name(), null, id,
                Node.ActivityStatus.name(), Field.activityStatusId.name(), IN_COMPLETED_STATUS,
                Relationship.IN_STATUS.name()
        );

        return controlActivityConverter.map(extractControlActivityById(id));
    }

    @Transactional
    public ControlActivityDTO addTemplate(TemplateOperationRequest request) {
        checkPermission(request.getId());

        customQueryService.createRelationship(
                Node.ControlActivity.name(), null, request.getId(),
                Node.Template.name(), null, request.getTemplateId(),
                Relationship.HAS_TEMPLATE.name()
        );

        return controlActivityConverter.map(extractControlActivityById(request.getId()));
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

        if (nonNull(request.getId())) {
            entity.setId(request.getId());
            LocalDateTime dateCreated = responseSessionService.getDateCreatedBySessionId(request.getId());
            entity.setDateCreated(dateCreated);
        }

        entity.setName(request.getName());
        entity.setAutoCreated(request.isAutoCreated());

        if (request.isAutoCreated()) {
            entity.setStatus(sessionStatusService.findById(IN_COMPLETED_STATUS));
        } else {
            entity.setStatus(sessionStatusService.findById(IN_PROCESS_STATUS));
        }

        Map<Long, QuestionExecutionEntity> questionExecutionIdMap = template.getGroups()
                .stream()
                .flatMap(qg -> qg.getQuestions().stream())
                .collect(toMap(QuestionExecutionEntity::getId, Function.identity()));

        questionExecutionIdMap.putAll(getQuestionExecutionMap(template.getConfig()));

        for (AnswerDTO answer : request.getAnswers()) {
            QuestionExecutionEntity questionExecution = questionExecutionIdMap.get(answer.getQuestionId());
            if (isNull(questionExecution))
                throw new EntityNotFoundException(Node.QuestionExecution.name(), answer.getQuestionId());

            AnswerEntity answerEntity = new AnswerEntity();
            answerEntity.setComment(answer.getComment());

            if (!isEmpty(answer.getValues())) {
                try {
                    answerEntity.setValues(mapper.writeValueAsString(answer.getValues()));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }

            if (nonNull(answer.getValueId())) {
//                answerEntity.setValueId(answer.getValueId());
            }

            answerEntity.setQuestionExecution(questionExecution);

            entity.getAnswers().add(answerEntity);
        }

        entity = responseSessionService.save(entity);

        customQueryService.createRelationship(
                Node.ResponseSession.name(), null, entity.getId(),
                Node.Template.name(), null, template.getId(),
                Relationship.TEMPLATED_BY.name()
        );

        customQueryService.createRelationship(
                Node.ControlActivity.name(), null, request.getControlActivityId(),
                Node.ResponseSession.name(), null, entity.getId(),
                Relationship.HAS_SESSION_RESPONSE.name()
        );

        customQueryService.createRelationship(
                Node.ResponseSession.name(), null, entity.getId(),
                Node.User.name(), null, UserUtils.getCurrentUserId(),
                Relationship.HAS_AUTHOR.name()
        );
    }

    public ResponseSessionDTO getSession(Long id) {
        return responseSessionConverter.map(responseSessionService.findById(id));
    }

    @Transactional
    public ControlActivityDTO deleteTemplate(TemplateOperationRequest request) {
        checkPermission(request.getId());

        ControlActivityEntity activity = extractControlActivityById(request.getId());

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
                Node.ControlActivity.name(), null, request.getId(),
                Node.Template.name(), null, request.getTemplateId(),
                Relationship.HAS_TEMPLATE.name()
        );

        return controlActivityConverter.map(activity);
    }

    @Transactional
    public ResponseSessionDTO changeStatus(ResponseSessionStatusRequest request) {
        checkPermission(request.getControlActivityId());

        ResponseSessionEntity entity = responseSessionService.findById(request.getResponseSessionId());

        if (IN_PROCESS_STATUS.equals(request.getSessionStatusId())) {
            entity.setReviewer(null);
        } else if (IN_COMPLETED_STATUS.equals(request.getSessionStatusId())) {
            entity.setReviewer(userService.findById(UserUtils.getCurrentUserId()));
        }
        entity.setStatus(sessionStatusService.findById(request.getSessionStatusId()));

        return responseSessionConverter.map(responseSessionService.save(entity));
    }

    private ControlActivityEntity extractControlActivityById(Long id) {
        ControlActivityDomain domain = controlActivityService.findById(id);

        ControlActivityEntity entity = new ControlActivityEntity();
        BeanUtils.copyProperties(domain, entity);

        entity.setActivityResponse(responseSessionService.findById(domain.getActivityResponseId()));
        entity.setAuthor(userService.findById(domain.getAuthorId()));
        entity.setStatus(activityStatusService.findById(domain.getStatusId()));
        entity.setTemplates(new HashSet<>(templateService.findByIds(domain.getTemplateIds())));
        entity.setMembers(new HashSet<>(userService.findByIds(domain.getMemberIds())));
        entity.setSessionResponses(new HashSet<>(responseSessionService.findByIds(domain.getSessionResponseIds())));

        return entity;
    }

    @Transactional
    public void update(UpdateControlActivityRequest request) {
        ControlActivityDomain domain = controlActivityService.findById(request.getId());
        ResponseSessionEntity activityResponse = responseSessionService.findById(domain.getActivityResponseId());

        Map<Long, QuestionExecutionEntity> questionExecutionIdMap = getQuestionExecutionMap(activityResponse.getTemplateConfig());

        request.getAnswers().forEach(a -> {

            AnswerEntity answer = activityResponse.getAnswers()
                    .stream()
                    .filter(ea -> a.getQuestionId().equals(ea.getQuestionExecution().getId()))
                    .findFirst()
                    .orElseGet(AnswerEntity::new);

            QuestionExecutionEntity questionExecution = questionExecutionIdMap.get(a.getQuestionId());
            answer.setQuestionExecution(questionExecution);
            answer.setComment(a.getComment());

            if (nonNull(a.getValueId())) {
//                answer.setValueId(a.getValueId());
            }

            if (!isEmpty(a.getValues())) {
                try {
                    answer.setValues(mapper.writeValueAsString(a.getValues()));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }

            if (isNull(answer.getId())) {
                activityResponse.getAnswers().add(answer);
            }
        });

        responseSessionService.save(activityResponse);
    }

    private Map<Long, QuestionExecutionEntity> getQuestionExecutionMap(TemplateConfigEntity templateConfig) {
        Map<Long, QuestionExecutionEntity> questionExecutionIdMap = new HashMap<>();
        templateConfig.getTypeQuestions().forEach((q -> questionExecutionIdMap.put(q.getId(), q)));
        templateConfig.getObjectFutureQuestions().forEach((q -> questionExecutionIdMap.put(q.getId(), q)));
        templateConfig.getAuthorityFeatureQuestions().forEach((q -> questionExecutionIdMap.put(q.getId(), q)));
        questionExecutionIdMap.put(
                templateConfig.getObjectQuestion().getId(),
                templateConfig.getObjectQuestion());
        if (nonNull(templateConfig.getAuthorityQuestion())) {
            questionExecutionIdMap.put(
                    templateConfig.getAuthorityQuestion().getId(),
                    templateConfig.getAuthorityQuestion());
        }
        return questionExecutionIdMap;
    }
}
