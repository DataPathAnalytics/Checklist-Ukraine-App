package com.datapath.checklistapp.service.web;

import com.datapath.checklistapp.dao.entity.*;
import com.datapath.checklistapp.dao.service.ControlActivityDaoService;
import com.datapath.checklistapp.dao.service.ResponseSessionDaoService;
import com.datapath.checklistapp.dao.service.UserDaoService;
import com.datapath.checklistapp.dao.service.classifier.SessionStatusDaoService;
import com.datapath.checklistapp.dto.ResponseSessionDTO;
import com.datapath.checklistapp.dto.request.response_session.ResponseSessionCreateRequest;
import com.datapath.checklistapp.dto.request.response_session.ResponseSessionPageableRequest;
import com.datapath.checklistapp.dto.request.response_session.ResponseSessionUpdateRequest;
import com.datapath.checklistapp.dto.request.response_session.StatusOperationRequest;
import com.datapath.checklistapp.dto.response.page.PageableResponse;
import com.datapath.checklistapp.exception.ValidationException;
import com.datapath.checklistapp.service.mapper.AnswerMapper;
import com.datapath.checklistapp.service.mapper.SessionMapper;
import com.datapath.checklistapp.service.web.abs.QuestionAnswerDataUtils;
import com.datapath.checklistapp.util.UserUtils;
import com.datapath.checklistapp.util.database.ResponseRole;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

import static com.datapath.checklistapp.util.Constants.IN_COMPLETED_STATUS;
import static com.datapath.checklistapp.util.Constants.IN_PROCESS_STATUS;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class ResponseSessionWebService implements QuestionAnswerDataUtils {

    private final ControlActivityDaoService controlActivityService;
    private final UserDaoService userService;
    private final ResponseSessionDaoService responseSessionService;
    private final SessionStatusDaoService sessionStatusService;
    private final AnswerMapper answerMapper;
    private final SessionMapper sessionMapper;

    @Override
    public AnswerMapper getAnswerMapper() {
        return answerMapper;
    }

    @Transactional
    public PageableResponse<ResponseSessionDTO> list(ResponseSessionPageableRequest request) {
        ControlActivityEntity activity = controlActivityService.findById(request.getControlActivityId());

        checkPermission(activity);

        Page<SessionEntity> page = responseSessionService.findResponseSessionByActivity(activity, request.getPage(), request.getSize());

        return new PageableResponse<>(
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.get()
                        .map(sessionMapper::map)
                        .collect(toList())
        );
    }

    @Transactional
    public ResponseSessionDTO create(ResponseSessionCreateRequest request) {
        ControlActivityEntity activity = controlActivityService.findById(request.getControlActivityId());

        checkPermission(activity);

        TemplateEntity template = activity.getTemplates().stream()
                .filter(t -> t.getId().equals(request.getTemplateId()))
                .findFirst()
                .orElseThrow(() -> new ValidationException("Not found template in control activity"));

        SessionEntity session = new SessionEntity();
        session.setRole(ResponseRole.SESSION_RESPONSE);
        session.setAuthor(userService.findById(UserUtils.getCurrentUserId()));

        session.setAutoCreated(request.isAutoCreated());
        if (request.isAutoCreated()) session.setStatus(sessionStatusService.findById(IN_COMPLETED_STATUS));
        else session.setStatus(sessionStatusService.findById(IN_PROCESS_STATUS));

        session.setNumber(getNextSessionNumber(activity.getSessionResponses()));
        session.setActivity(activity);

        session.setTemplate(template);

        Map<Integer, QuestionExecutionEntity> questions = getQuestionExecutionMap(template.getConfig());
        questions.putAll(getQuestionExecutionMap(template));

        Set<AnswerEntity> answers = updateAnswers(request.getAnswers(), session, questions);
        session.getAnswers().clear();
        session.getAnswers().addAll(answers);

        return sessionMapper.map(responseSessionService.save(session));
    }

    @Transactional
    public ResponseSessionDTO update(ResponseSessionUpdateRequest request) {
        SessionEntity session = responseSessionService.findById(request.getId());

        checkPermission(session.getActivity());

        session.setDateModified(LocalDateTime.now());

        Map<Integer, QuestionExecutionEntity> questions = getQuestionExecutionMap(session.getTemplate().getConfig());
        questions.putAll(getQuestionExecutionMap(session.getTemplate()));

        Set<AnswerEntity> answers = updateAnswers(request.getAnswers(), session, questions);
        session.getAnswers().clear();
        session.getAnswers().addAll(answers);

        return sessionMapper.map(responseSessionService.save(session));
    }

    @Transactional
    public ResponseSessionDTO get(Integer id) {
        SessionEntity entity = responseSessionService.findById(id);
        checkPermission(entity.getActivity());
        return sessionMapper.map(entity);
    }

    @Transactional
    public ResponseSessionDTO status(StatusOperationRequest request) {
        SessionEntity entity = responseSessionService.findById(request.getId());
        checkPermission(entity.getActivity());

        if (IN_PROCESS_STATUS.equals(request.getStatusId())) {
            entity.setReviewer(null);
        } else if (IN_COMPLETED_STATUS.equals(request.getStatusId())) {
            entity.setReviewer(userService.findById(UserUtils.getCurrentUserId()));
        }
        entity.setStatus(sessionStatusService.findById(request.getStatusId()));

        return sessionMapper.map(responseSessionService.save(entity));
    }

    private Integer getNextSessionNumber(Set<SessionEntity> sessions) {
        return sessions.stream()
                .map(SessionEntity::getNumber)
                .max(Integer::compareTo).orElse(0) + 1;
    }
}
