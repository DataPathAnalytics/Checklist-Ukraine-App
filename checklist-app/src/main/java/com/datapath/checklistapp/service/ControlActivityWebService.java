package com.datapath.checklistapp.service;

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
import com.datapath.checklistapp.util.database.Entity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    private final AnswerConverter answerConverter;
    private final ControlActivityConverter controlActivityConverter;
    private final ResponseSessionConverter responseSessionConverter;

    @Transactional
    public List<ControlActivityDTO> list() {
        List<ControlActivityEntity> activities;

        if (UserUtils.hasRole(METHODOLOGIST_ROLE)) {
            activities = controlActivityService.findAll(null);
        } else {
            activities = controlActivityService.findAll(UserUtils.getCurrentUserId());
        }

        return activities
                .stream()
                .map(c -> {
                    ControlActivityDTO dto = new ControlActivityDTO();
                    dto.setId(c.getId());
                    dto.setStatusId(c.getStatus().getId());
                    dto.setActivity(responseSessionConverter.map(c.getActivityResponse()));
                    return dto;
                })
                .sorted((dto1, dto2) -> {
                    LocalDateTime t1 = dto1.getActivity().getDateModified();
                    LocalDateTime t2 = dto2.getActivity().getDateModified();
                    return t1.compareTo(t2);
                })
                .collect(toList());
    }

    @Transactional
    public ControlActivityDTO create(CreateControlActivityRequest request) {
        TemplateConfigEntity config = templateConfigService.findById(request.getTemplateConfigId());

        ResponseSessionEntity activityResponse = new ResponseSessionEntity();
        activityResponse.setAuthor(userService.findById(UserUtils.getCurrentUserId()));
        activityResponse.setMembers(userService.findByIds(request.getMemberIds()));
        activityResponse.setTemplateConfig(config);

        Map<Long, QuestionExecutionEntity> questionExecutionIdMap = getQuestionExecutionMap(config);

        request.getAnswers().forEach(a -> {
            QuestionExecutionEntity questionExecution = questionExecutionIdMap.get(a.getQuestionId());

            AnswerEntity answerEntity = new AnswerEntity();
            answerEntity.setComment(a.getComment());
            answerEntity.setQuestionExecution(questionExecution);
            answerEntity.setValues(answerConverter.toJson(a));

            activityResponse.getAnswers().add(answerEntity);
        });

        ControlActivityEntity activityEntity = new ControlActivityEntity();
        activityEntity.setStatus(activityStatusService.findById(IN_PROCESS_STATUS));
        activityEntity.setTemplates(templateService.findByIds(request.getTemplateIds()));
        activityEntity.setActivityResponse(activityResponse);

        return controlActivityConverter.map(controlActivityService.save(activityEntity));
    }

    @Transactional
    public ControlActivityDTO update(UpdateControlActivityRequest request) {
        ControlActivityEntity entity = controlActivityService.findById(request.getId());
        ResponseSessionEntity activityResponse = entity.getActivityResponse();

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
            answer.setValues(answerConverter.toJson(a));

            if (isNull(answer.getId())) {
                activityResponse.getAnswers().add(answer);
            }
        });

        if (!isEmpty(request.getMemberIds())) {
            activityResponse.setMembers(userService.findByIds(request.getMemberIds()));
        }

        return controlActivityConverter.map(controlActivityService.save(entity));
    }

    @Transactional
    public ControlActivityDTO get(Long id) {
        return controlActivityConverter.map(controlActivityService.findById(id));
    }

    @Transactional
    public ControlActivityDTO complete(Long id) {
        ControlActivityEntity entity = controlActivityService.findById(id);

        checkPermission(entity);

        entity.setStatus(activityStatusService.findById(IN_PROCESS_STATUS));

        return controlActivityConverter.map(controlActivityService.save(entity));
    }

    @Transactional
    public ControlActivityDTO addTemplate(TemplateOperationRequest request) {
        ControlActivityEntity entity = controlActivityService.findById(request.getId());

        checkPermission(entity);

        entity.getTemplates().add(templateService.findById(request.getTemplateId()));

        return controlActivityConverter.map(controlActivityService.save(entity));
    }

    @Transactional
    public ControlActivityDTO deleteTemplate(TemplateOperationRequest request) {
        ControlActivityEntity entity = controlActivityService.findById(request.getId());

        checkPermission(entity);

        boolean existsChecklist = entity.getSessionResponses().stream()
                .anyMatch(c -> request.getTemplateId().equals(c.getTemplate().getId()));

        if (!existsChecklist) {
            entity.setTemplates(
                    entity.getTemplates().stream()
                            .filter(t -> !request.getTemplateId().equals(t.getId()))
                            .collect(toSet())
            );

            entity = controlActivityService.save(entity);
        }

        return controlActivityConverter.map(entity);
    }

    @Transactional
    public SessionPageDTO getSessions(Long activityId, int page, int size) {
        SessionPageDTO dto = new SessionPageDTO();

        ControlActivityEntity activity = controlActivityService.findById(activityId);
        Set<ResponseSessionEntity> sessions = activity.getSessionResponses();

        dto.setTotalCount(sessions.size());
        dto.setTotalPageCount((int) Math.ceil((double) sessions.size() / size));
        dto.setCurrentPage(page);
        dto.setPageSize(size);
        dto.setSessions(
                sessions.stream()
                        .skip(page * size)
                        .limit(size)
                        .map(responseSessionConverter::map)
                        .collect(toList())
        );

        return dto;
    }

    @Transactional
    public ResponseSessionDTO saveSession(SaveResponseSessionRequest request) {
        ControlActivityEntity activity;
        ResponseSessionEntity session;

        if (nonNull(request.getId())) {
            session = responseSessionService.findById(request.getId());
            activity = session.getActivity();
        } else {
            session = new ResponseSessionEntity();

            session.setAutoCreated(request.isAutoCreated());
            if (request.isAutoCreated()) {
                session.setStatus(sessionStatusService.findById(IN_COMPLETED_STATUS));
            } else {
                session.setStatus(sessionStatusService.findById(IN_PROCESS_STATUS));
            }

            activity = controlActivityService.findById(request.getControlActivityId());
            session.setNumber(getNextSessionNumber(activity.getSessionResponses()));
            session.setActivity(activity);
        }

        checkPermission(activity);

        TemplateEntity template = activity.getTemplates().stream()
                .filter(t -> t.getId().equals(request.getTemplateId()))
                .findFirst()
                .orElseThrow(() -> new ValidationException("Not found template in control activity"));

        Map<Long, QuestionExecutionEntity> questionExecutionIdMap = template.getGroups()
                .stream()
                .flatMap(qg -> qg.getQuestions().stream())
                .collect(toMap(QuestionExecutionEntity::getId, Function.identity()));

        questionExecutionIdMap.putAll(getQuestionExecutionMap(template.getConfig()));

        Set<AnswerEntity> answers = new HashSet<>();
        for (AnswerDTO answer : request.getAnswers()) {
            QuestionExecutionEntity questionExecution = questionExecutionIdMap.get(answer.getQuestionId());
            if (isNull(questionExecution))
                throw new EntityNotFoundException(Entity.QuestionExecution.name(), answer.getQuestionId());

            AnswerEntity answerEntity = new AnswerEntity();
            answerEntity.setComment(answer.getComment());
            answerEntity.setValues(answerConverter.toJson(answer));
            answerEntity.setQuestionExecution(questionExecution);
            answers.add(answerEntity);
        }
        session.setAnswers(answers);
        session.setTemplate(template);
        session.setAuthor(userService.findById(UserUtils.getCurrentUserId()));

        return responseSessionConverter.map(responseSessionService.save(session));
    }

    public ResponseSessionDTO getSession(Long id) {
        return responseSessionConverter.map(responseSessionService.findById(id));
    }

    @Transactional
    public ResponseSessionDTO changeStatus(ResponseSessionStatusRequest request) {
        ResponseSessionEntity entity = responseSessionService.findById(request.getResponseSessionId());
        checkPermission(entity.getActivity());

        if (IN_PROCESS_STATUS.equals(request.getSessionStatusId())) {
            entity.setReviewer(null);
        } else if (IN_COMPLETED_STATUS.equals(request.getSessionStatusId())) {
            entity.setReviewer(userService.findById(UserUtils.getCurrentUserId()));
        }
        entity.setStatus(sessionStatusService.findById(request.getSessionStatusId()));

        return responseSessionConverter.map(responseSessionService.save(entity));
    }

    private void checkPermission(ControlActivityEntity entity) {
        Long currentUserId = UserUtils.getCurrentUserId();

        ResponseSessionEntity activityResponse = entity.getActivityResponse();

        Set<Long> members = activityResponse.getMembers().stream()
                .map(UserEntity::getId)
                .collect(toSet());
        members.add(activityResponse.getAuthor().getId());

        if (!members.contains(currentUserId) && !UserUtils.hasRole(METHODOLOGIST_ROLE))
            throw new PermissionException("You can't modify this control event");
    }

    private Map<Long, QuestionExecutionEntity> getQuestionExecutionMap(TemplateConfigEntity templateConfig) {
        return templateConfig.getQuestions().stream()
                .collect(toMap(QuestionExecutionEntity::getId, Function.identity()));
    }

    private Integer getNextSessionNumber(Set<ResponseSessionEntity> sessions) {
        return sessions.stream()
                .map(ResponseSessionEntity::getNumber)
                .max(Integer::compareTo).orElse(1) + 1;
    }
}
