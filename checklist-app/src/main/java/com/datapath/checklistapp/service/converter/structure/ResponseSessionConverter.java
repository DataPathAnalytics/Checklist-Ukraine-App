package com.datapath.checklistapp.service.converter.structure;

import com.datapath.checklistapp.dao.entity.AnswerEntity;
import com.datapath.checklistapp.dao.entity.QuestionExecutionEntity;
import com.datapath.checklistapp.dao.entity.QuestionGroupEntity;
import com.datapath.checklistapp.dao.entity.ResponseSessionEntity;
import com.datapath.checklistapp.dto.GroupQuestionsDTO;
import com.datapath.checklistapp.dto.QuestionExecutionDTO;
import com.datapath.checklistapp.dto.ResponseSessionDTO;
import com.datapath.checklistapp.exception.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.datapath.checklistapp.util.Constants.*;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
@AllArgsConstructor
public class ResponseSessionConverter {

    private final QuestionConverter questionConverter;

    public ResponseSessionDTO mapResponseSessionBaseInfo(ResponseSessionEntity entity) {
        ResponseSessionDTO dto = new ResponseSessionDTO();

        BeanUtils.copyProperties(entity, dto);
        dto.setAuthorId(entity.getAuthor().getId());
        dto.setTemplateId(entity.getTemplate().getId());
        dto.setTemplateName(entity.getTemplate().getName());
        dto.setSessionStatusId(entity.getStatus().getSessionStatusId());

        if (nonNull(entity.getReviewer())) {
            dto.setReviewerId(entity.getReviewer().getId());
        }

        return dto;
    }

    public ResponseSessionDTO map(ResponseSessionEntity entity) {
        ResponseSessionDTO dto = mapResponseSessionBaseInfo(entity);

        Map<Long, AnswerEntity> answerQuestionId = entity.getAnswers()
                .stream()
                .collect(toMap(a -> a.getQuestionExecution().getId(), Function.identity()));

        QuestionExecutionEntity objectQuestion = entity.getTemplate().getConfig().getQuestionExecutions()
                .stream()
                .filter(q -> OBJECT_QUESTION_TYPE.equals(q.getQuestion().getType().getTypeId()))
                .findFirst()
                .orElseThrow(() -> new ValidationException("Required object question not found. Response session id: " + entity.getId()));

        List<QuestionExecutionDTO> featureQuestions = entity.getTemplate().getConfig().getQuestionExecutions()
                .stream()
                .filter(q -> FEATURE_QUESTION_TYPE.equals(q.getQuestion().getType().getTypeId()))
                .map(q -> questionConverter.map(q, answerQuestionId.get(q.getId())))
                .collect(toList());

        List<QuestionExecutionDTO> typeQuestions = entity.getTemplate().getConfig().getQuestionExecutions()
                .stream()
                .filter(q -> SESSION_QUESTION_TYPE.equals(q.getQuestion().getType().getTypeId()))
                .map(q -> questionConverter.map(q, answerQuestionId.get(q.getId())))
                .collect(toList());

        List<QuestionExecutionDTO> ungroupedQuestions = entity.getTemplate().getGroups()
                .stream()
                .filter(g -> UNGROUPED_NAME.equals(g.getName()))
                .flatMap(g -> g.getQuestions().stream())
                .sorted(Comparator.comparing(QuestionExecutionEntity::getOrderNumber))
                .map(q -> questionConverter.map(q, answerQuestionId.get(q.getId())))
                .collect(toList());

        List<GroupQuestionsDTO> questions = entity.getTemplate().getGroups()
                .stream()
                .filter(g -> !UNGROUPED_NAME.equals(g.getName()))
                .sorted(Comparator.comparing(QuestionGroupEntity::getOrderNumber))
                .map(g -> {
                    GroupQuestionsDTO groupQuestionsDTO = new GroupQuestionsDTO();
                    groupQuestionsDTO.setGroupName(g.getName());
                    groupQuestionsDTO.setQuestions(
                            g.getQuestions().stream()
                                    .sorted(Comparator.comparing(QuestionExecutionEntity::getOrderNumber))
                                    .map(q -> questionConverter.map(q, answerQuestionId.get(q.getId())))
                                    .collect(toList())
                    );
                    return groupQuestionsDTO;
                })
                .collect(toList());

        dto.setObjectQuestion(questionConverter.map(objectQuestion, answerQuestionId.get(objectQuestion.getId())));
        dto.setObjectFeatureQuestions(featureQuestions);
        dto.setTypeQuestions(typeQuestions);
        dto.setQuestions(questions);
        dto.setUngroupedQuestions(ungroupedQuestions);

        return dto;
    }
}
