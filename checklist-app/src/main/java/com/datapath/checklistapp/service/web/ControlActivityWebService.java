package com.datapath.checklistapp.service.web;

import com.datapath.checklistapp.dao.entity.*;
import com.datapath.checklistapp.dao.service.ControlActivityDaoService;
import com.datapath.checklistapp.dao.service.TemplateConfigDaoService;
import com.datapath.checklistapp.dao.service.TemplateDaoService;
import com.datapath.checklistapp.dao.service.UserDaoService;
import com.datapath.checklistapp.dao.service.classifier.ActivityStatusDaoService;
import com.datapath.checklistapp.dto.ControlActivityDTO;
import com.datapath.checklistapp.dto.request.activity.CreateRequest;
import com.datapath.checklistapp.dto.request.activity.TemplateOperationRequest;
import com.datapath.checklistapp.dto.request.activity.UpdateRequest;
import com.datapath.checklistapp.dto.request.page.PageableRequest;
import com.datapath.checklistapp.dto.response.page.PageableResponse;
import com.datapath.checklistapp.service.mapper.AnswerMapper;
import com.datapath.checklistapp.service.mapper.ControlActivityMapper;
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

import static com.datapath.checklistapp.util.Constants.*;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@AllArgsConstructor
public class ControlActivityWebService implements QuestionAnswerDataUtils {

    private final ControlActivityDaoService controlActivityService;
    private final UserDaoService userService;
    private final ActivityStatusDaoService activityStatusService;
    private final TemplateDaoService templateService;
    private final TemplateConfigDaoService templateConfigService;

    private final AnswerMapper answerMapper;
    private final ControlActivityMapper controlActivityMapper;
    private final SessionMapper sessionMapper;

    @Override
    public AnswerMapper getAnswerMapper() {
        return answerMapper;
    }

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
                            dto.setActivity(sessionMapper.map(c.getActivityResponse()));
                            return dto;
                        })
                        .collect(toList())
        );
    }

    @Transactional
    public ControlActivityDTO create(CreateRequest request) {
        TemplateConfigEntity config = templateConfigService.findById(request.getTemplateConfigId());

        SessionEntity activityResponse = new SessionEntity();
        activityResponse.setRole(ResponseRole.ACTIVITY_RESPONSE);
        activityResponse.setAuthor(userService.findById(UserUtils.getCurrentUserId()));
        activityResponse.setMembers(userService.findByIds(request.getMemberIds()));
        activityResponse.setTemplateConfig(config);

        Map<Integer, QuestionExecutionEntity> questionExecutionIdMap = getQuestionExecutionMap(config);

        request.getAnswers().forEach(a -> {
            String values = answerMapper.toJson(a);

            if (nonNull(values)) {
                QuestionExecutionEntity questionExecution = questionExecutionIdMap.get(a.getQuestionId());

                AnswerEntity answerEntity = new AnswerEntity();
                answerEntity.setComment(a.getComment());
                answerEntity.setQuestionExecution(questionExecution);

                answerEntity.setValues(values);

                activityResponse.getAnswers().add(answerEntity);
            }
        });

        ControlActivityEntity activityEntity = new ControlActivityEntity();
        activityEntity.setStatus(activityStatusService.findById(IN_PROCESS_STATUS));
        activityEntity.setTemplates(templateService.findByIds(request.getTemplateIds()));
        activityEntity.setActivityResponse(activityResponse);

        activityResponse.setActivity(activityEntity);

        return controlActivityMapper.map(controlActivityService.save(activityEntity));
    }

    @Transactional
    public ControlActivityDTO update(UpdateRequest request) {
        ControlActivityEntity entity = controlActivityService.findById(request.getId());
        checkPermission(entity);

        SessionEntity session = entity.getActivityResponse();
        session.setDateModified(LocalDateTime.now());

        Map<Integer, QuestionExecutionEntity> questions = getQuestionExecutionMap(session.getTemplateConfig());

        Set<AnswerEntity> answers = updateAnswers(request.getAnswers(), session, questions);
        session.getAnswers().clear();
        session.getAnswers().addAll(answers);

        if (!isEmpty(request.getMemberIds()))
            session.setMembers(userService.findByIds(request.getMemberIds()));
        else
            session.getMembers().clear();

        return controlActivityMapper.map(controlActivityService.save(entity));
    }

    @Transactional
    public ControlActivityDTO get(Integer id) {
        ControlActivityEntity entity = controlActivityService.findById(id);
        checkPermission(entity);
        return controlActivityMapper.map(entity);
    }

    @Transactional
    public ControlActivityDTO complete(Integer id) {
        ControlActivityEntity entity = controlActivityService.findById(id);

        checkPermission(entity);

        entity.setStatus(activityStatusService.findById(IN_COMPLETED_STATUS));

        return controlActivityMapper.map(controlActivityService.save(entity));
    }

    @Transactional
    public ControlActivityDTO addTemplate(TemplateOperationRequest request) {
        ControlActivityEntity entity = controlActivityService.findById(request.getControlActivityId());

        checkPermission(entity);

        entity.getTemplates().add(templateService.findById(request.getTemplateId()));

        return controlActivityMapper.map(controlActivityService.save(entity));
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

        return controlActivityMapper.map(entity);
    }
}
