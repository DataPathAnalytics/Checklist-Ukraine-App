package com.datapath.checklistapp.service;

import com.datapath.checklistapp.dao.entity.*;
import com.datapath.checklistapp.dao.service.*;
import com.datapath.checklistapp.dao.service.classifier.ActivityStatusDaoService;
import com.datapath.checklistapp.dao.service.classifier.SessionStatusDaoService;
import com.datapath.checklistapp.dto.AnswerDTO;
import com.datapath.checklistapp.dto.ControlActivityDTO;
import com.datapath.checklistapp.dto.ResponseSessionDTO;
import com.datapath.checklistapp.dto.request.activity.*;
import com.datapath.checklistapp.dto.request.page.PageableRequest;
import com.datapath.checklistapp.dto.request.page.SessionPageableRequest;
import com.datapath.checklistapp.dto.response.page.PageableResponse;
import com.datapath.checklistapp.exception.EntityNotFoundException;
import com.datapath.checklistapp.exception.PermissionException;
import com.datapath.checklistapp.exception.ValidationException;
import com.datapath.checklistapp.service.converter.structure.AnswerConverter;
import com.datapath.checklistapp.service.converter.structure.ControlActivityConverter;
import com.datapath.checklistapp.service.converter.structure.ResponseSessionConverter;
import com.datapath.checklistapp.util.UserUtils;
import com.datapath.checklistapp.util.database.Entity;
import com.datapath.checklistapp.util.database.SessionPlace;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public PageableResponse<ControlActivityDTO> list(PageableRequest request) {
        Page<ControlActivityEntity> page;

        if (UserUtils.hasRole(METHODOLOGIST_ROLE)) {
            page = controlActivityService.findAll(null, request);
        } else {
            page = controlActivityService.findAll(UserUtils.getCurrentUserId(), request);
        }

        return new PageableResponse<>(
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.get()
                        .map(c -> {
                            ControlActivityDTO dto = new ControlActivityDTO();
                            dto.setId(c.getId());
                            dto.setStatusId(c.getStatus().getId());
                            dto.setActivity(responseSessionConverter.map(c.getActivityResponse()));
                            return dto;
                        })
                        .collect(toList())
        );
    }

    @Transactional
    public ControlActivityDTO create(CreateControlActivityRequest request) {
        TemplateConfigEntity config = templateConfigService.findById(request.getTemplateConfigId());

        SessionEntity activityResponse = new SessionEntity();
        activityResponse.setPlace(SessionPlace.ACTIVITY_RESPONSE);
        activityResponse.setAuthor(userService.findById(UserUtils.getCurrentUserId()));
        activityResponse.setMembers(userService.findByIds(request.getMemberIds()));
        activityResponse.setTemplateConfig(config);

        Map<Integer, QuestionExecutionEntity> questionExecutionIdMap = getQuestionExecutionMap(config);

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

        activityResponse.setActivity(activityEntity);

        return controlActivityConverter.map(controlActivityService.save(activityEntity));
    }

    @Transactional
    public ControlActivityDTO update(UpdateControlActivityRequest request) {
        ControlActivityEntity entity = controlActivityService.findById(request.getId());
        SessionEntity session = entity.getActivityResponse();

        Map<Integer, QuestionExecutionEntity> questions = getQuestionExecutionMap(session.getTemplateConfig());

        Set<AnswerEntity> answers = updateAnswers(request.getAnswers(), session, questions);
        session.getAnswers().clear();
        session.getAnswers().addAll(answers);

        if (!isEmpty(request.getMemberIds())) {
            session.setMembers(userService.findByIds(request.getMemberIds()));
        }

        return controlActivityConverter.map(controlActivityService.save(entity));
    }

    @Transactional
    public ControlActivityDTO get(Integer id) {
        return controlActivityConverter.map(controlActivityService.findById(id));
    }

    @Transactional
    public ControlActivityDTO complete(Integer id) {
        ControlActivityEntity entity = controlActivityService.findById(id);

        checkPermission(entity);

        entity.setStatus(activityStatusService.findById(IN_COMPLETED_STATUS));

        return controlActivityConverter.map(controlActivityService.save(entity));
    }

    @Transactional
    public ControlActivityDTO addTemplate(TemplateOperationRequest request) {
        ControlActivityEntity entity = controlActivityService.findById(request.getControlActivityId());

        checkPermission(entity);

        entity.getTemplates().add(templateService.findById(request.getTemplateId()));

        return controlActivityConverter.map(controlActivityService.save(entity));
    }

    @Transactional
    public ControlActivityDTO deleteTemplate(TemplateOperationRequest request) {
        ControlActivityEntity entity = controlActivityService.findById(request.getControlActivityId());

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
    public PageableResponse<ResponseSessionDTO> getSessions(SessionPageableRequest request) {
        ControlActivityEntity activity = controlActivityService.findById(request.getControlActivityId());

        checkPermission(activity);

        Page<SessionEntity> page = responseSessionService.findResponseSessionByActivity(activity, request.getPage(), request.getSize());

        return new PageableResponse<>(
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.get()
                        .map(responseSessionConverter::map)
                        .collect(toList())
        );
    }

    @Transactional
    public ResponseSessionDTO saveSession(SaveResponseSessionRequest request) {
        ControlActivityEntity activity = controlActivityService.findById(request.getControlActivityId());

        checkPermission(activity);

        TemplateEntity template = activity.getTemplates().stream()
                .filter(t -> t.getId().equals(request.getTemplateId()))
                .findFirst()
                .orElseThrow(() -> new ValidationException("Not found template in control activity"));

        SessionEntity session;

        if (nonNull(request.getId())) {
            session = responseSessionService.findByIdAndActivity(request.getId(), activity);
        } else {
            session = new SessionEntity();
            session.setPlace(SessionPlace.SESSION_RESPONSE);
            session.setAutoCreated(request.isAutoCreated());
            session.setAuthor(userService.findById(UserUtils.getCurrentUserId()));

            if (request.isAutoCreated()) {
                session.setStatus(sessionStatusService.findById(IN_COMPLETED_STATUS));
            } else {
                session.setStatus(sessionStatusService.findById(IN_PROCESS_STATUS));
            }

            session.setNumber(getNextSessionNumber(activity.getSessionResponses()));
            session.setActivity(activity);

            session.setTemplate(template);
        }

        Map<Integer, QuestionExecutionEntity> questions = getQuestionExecutionMap(template.getConfig());
        questions.putAll(getQuestionExecutionMap(template));

        Set<AnswerEntity> answers = updateAnswers(request.getAnswers(), session, questions);
        session.getAnswers().clear();
        session.getAnswers().addAll(answers);

        return responseSessionConverter.map(responseSessionService.save(session));
    }

    private Set<AnswerEntity> updateAnswers(List<AnswerDTO> answerDtos, SessionEntity session, Map<Integer, QuestionExecutionEntity> questions) {
        Set<AnswerEntity> answers = new HashSet<>();
        answerDtos.forEach(dto -> {

            QuestionExecutionEntity questionExecution = questions.get(dto.getQuestionId());
            if (isNull(questionExecution))
                throw new EntityNotFoundException(Entity.QuestionExecution.name(), dto.getQuestionId());

            //TODO: needs review after adding multiple answers to question
            AnswerEntity answer = session.getAnswers()
                    .stream()
                    .filter(ea -> dto.getQuestionId().equals(ea.getQuestionExecution().getId()))
                    .findFirst()
                    .orElseGet(AnswerEntity::new);

            answer.setQuestionExecution(questionExecution);
            answer.setComment(dto.getComment());
            answer.setValues(answerConverter.toJson(dto));

            answers.add(answer);
        });

        return answers;
    }

    @Transactional
    public ResponseSessionDTO getSession(Integer id) {
        return responseSessionConverter.map(responseSessionService.findById(id));
    }

    @Transactional
    public ResponseSessionDTO changeStatus(ResponseSessionStatusRequest request) {
        SessionEntity entity = responseSessionService.findById(request.getResponseSessionId());
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
        Integer currentUserId = UserUtils.getCurrentUserId();

        SessionEntity activityResponse = entity.getActivityResponse();

        Set<Integer> members = activityResponse.getMembers().stream()
                .map(UserEntity::getId)
                .collect(toSet());
        members.add(activityResponse.getAuthor().getId());

        if (!members.contains(currentUserId) && !UserUtils.hasRole(METHODOLOGIST_ROLE))
            throw new PermissionException("You can't modify this control event");
    }

    private Map<Integer, QuestionExecutionEntity> getQuestionExecutionMap(TemplateConfigEntity templateConfig) {
        return templateConfig.getQuestions().stream()
                .collect(toMap(QuestionExecutionEntity::getId, Function.identity()));
    }

    private Map<Integer, QuestionExecutionEntity> getQuestionExecutionMap(TemplateEntity template) {
        return template.getGroups()
                .stream()
                .flatMap(qg -> qg.getQuestions().stream())
                .collect(toMap(QuestionExecutionEntity::getId, Function.identity()));
    }

    private Integer getNextSessionNumber(Set<SessionEntity> sessions) {
        return sessions.stream()
                .map(SessionEntity::getNumber)
                .max(Integer::compareTo).orElse(0) + 1;
    }
}
