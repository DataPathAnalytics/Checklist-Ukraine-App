package com.datapath.checklistapp.service.web.abs;

import com.datapath.checklistapp.dao.entity.*;
import com.datapath.checklistapp.dto.AnswerDTO;
import com.datapath.checklistapp.exception.EntityNotFoundException;
import com.datapath.checklistapp.exception.PermissionException;
import com.datapath.checklistapp.service.mapper.AnswerMapper;
import com.datapath.checklistapp.util.UserUtils;
import com.datapath.checklistapp.util.database.Entity;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static com.datapath.checklistapp.util.Constants.METHODOLOGIST_ROLE;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

public interface QuestionAnswerDataUtils {

    AnswerMapper getAnswerMapper();

    default void checkPermission(ControlActivityEntity entity) {
        Integer currentUserId = UserUtils.getCurrentUserId();

        SessionEntity activityResponse = entity.getActivityResponse();

        Set<Integer> members = activityResponse.getMembers().stream()
                .map(UserEntity::getId)
                .collect(toSet());
        members.add(activityResponse.getAuthor().getId());

        if (!members.contains(currentUserId) && !UserUtils.hasRole(METHODOLOGIST_ROLE))
            throw new PermissionException("You can't modify this control event");
    }

    default Map<Integer, QuestionExecutionEntity> getQuestionExecutionMap(TemplateConfigEntity templateConfig) {
        return templateConfig.getQuestions().stream()
                .collect(toMap(QuestionExecutionEntity::getId, Function.identity()));
    }

    default Map<Integer, QuestionExecutionEntity> getQuestionExecutionMap(TemplateEntity template) {
        return template.getGroups()
                .stream()
                .flatMap(qg -> qg.getQuestions().stream())
                .collect(toMap(QuestionExecutionEntity::getId, Function.identity()));
    }

    default Set<AnswerEntity> updateAnswers(List<AnswerDTO> answerDtos, SessionEntity session, Map<Integer, QuestionExecutionEntity> questions) {
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
            answer.setValues(getAnswerMapper().toJson(dto));

            answers.add(answer);
        });

        return answers;
    }
}
