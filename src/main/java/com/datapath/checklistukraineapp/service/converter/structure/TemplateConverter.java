package com.datapath.checklistukraineapp.service.converter.structure;

import com.datapath.checklistukraineapp.dao.entity.QuestionExecutionEntity;
import com.datapath.checklistukraineapp.dao.entity.QuestionGroupEntity;
import com.datapath.checklistukraineapp.dao.entity.TemplateConfigEntity;
import com.datapath.checklistukraineapp.dao.entity.TemplateEntity;
import com.datapath.checklistukraineapp.dto.GroupQuestionsDTO;
import com.datapath.checklistukraineapp.dto.TemplateDTO;
import com.datapath.checklistukraineapp.exception.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Optional;

import static com.datapath.checklistukraineapp.util.Constants.*;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class TemplateConverter {

    private final QuestionConverter questionConverter;

    public TemplateDTO map(TemplateConfigEntity entity) {
        TemplateDTO dto = new TemplateDTO();

        BeanUtils.copyProperties(entity, dto);
        dto.setTemplateType(entity.getType().getTemplateTypeId());
        dto.setFolderId(entity.getFolder().getId());
        dto.setAuthorId(entity.getAuthor().getId());

        QuestionExecutionEntity objectQuestion = entity.getQuestionExecutions().stream()
                .filter(qe -> OBJECT_QUESTION_TYPE.equals(qe.getQuestion().getType().getQuestionTypeId()))
                .findFirst()
                .orElseThrow(() -> new ValidationException("Not found required object question"));

        dto.setObjectQuestion(questionConverter.map(objectQuestion));

        dto.setObjectFeatureQuestions(
                entity.getQuestionExecutions().stream()
                        .filter(qe -> FEATURE_QUESTION_TYPE.equals(qe.getQuestion().getType().getQuestionTypeId()))
                        .sorted(Comparator.comparing(QuestionExecutionEntity::getOrderNumber))
                        .map(questionConverter::map)
                        .collect(toList())
        );

        dto.setTypeQuestions(
                entity.getQuestionExecutions().stream()
                        .filter(qe -> ACTIVITY_QUESTION_TYPE.equals(qe.getQuestion().getType().getQuestionTypeId()) ||
                                SESSION_QUESTION_TYPE.equals(qe.getQuestion().getType().getQuestionTypeId()))
                        .sorted(Comparator.comparing(QuestionExecutionEntity::getOrderNumber))
                        .map(questionConverter::map)
                        .collect(toList())
        );

        dto.setAuthorityQuestions(
                entity.getQuestionExecutions().stream()
                        .filter(qe -> AUTHORITY_QUESTION_TYPE.equals(qe.getQuestion().getType().getQuestionTypeId()))
                        .sorted(Comparator.comparing(QuestionExecutionEntity::getOrderNumber))
                        .map(questionConverter::map)
                        .collect(toList())
        );

        return dto;
    }

    public TemplateDTO map(TemplateEntity entity) {
        TemplateDTO dto = new TemplateDTO();

        BeanUtils.copyProperties(entity, dto);
        dto.setTemplateConfigId(entity.getConfig().getId());
        dto.setFolderId(entity.getFolder().getId());
        dto.setAuthorId(entity.getAuthor().getId());

        QuestionExecutionEntity objectQuestion = entity.getConfig()
                .getQuestionExecutions()
                .stream()
                .filter(qe -> OBJECT_QUESTION_TYPE.equals(qe.getQuestion().getType().getQuestionTypeId()))
                .findFirst()
                .orElseThrow(() -> new ValidationException("Not found required object question"));

        dto.setObjectQuestion(questionConverter.map(objectQuestion));

        dto.setObjectFeatureQuestions(
                entity.getConfig().getQuestionExecutions().stream()
                        .filter(qe -> FEATURE_QUESTION_TYPE.equals(qe.getQuestion().getType().getQuestionTypeId()))
                        .sorted(Comparator.comparing(QuestionExecutionEntity::getOrderNumber))
                        .map(questionConverter::map)
                        .collect(toList())
        );

        dto.setTypeQuestions(
                entity.getConfig().getQuestionExecutions().stream()
                        .filter(qe -> ACTIVITY_QUESTION_TYPE.equals(qe.getQuestion().getType().getQuestionTypeId()) ||
                                SESSION_QUESTION_TYPE.equals(qe.getQuestion().getType().getQuestionTypeId()))
                        .sorted(Comparator.comparing(QuestionExecutionEntity::getOrderNumber))
                        .map(questionConverter::map)
                        .collect(toList())
        );

        Optional<QuestionGroupEntity> ungrouped = entity.getGroups().stream()
                .filter(g -> UNGROUPED_NAME.equals(g.getName()))
                .findFirst();

        ungrouped.ifPresent(questionGroupEntity -> dto.setUngroupedQuestions(
                questionGroupEntity.getQuestions().stream()
                        .sorted(Comparator.comparing(QuestionExecutionEntity::getOrderNumber))
                        .map(questionConverter::map)
                        .collect(toList())
        ));

        dto.setQuestions(
                entity.getGroups().stream()
                        .filter(g -> !UNGROUPED_NAME.equals(g.getName()))
                        .sorted(Comparator.comparing(QuestionGroupEntity::getOrderNumber))
                        .map(g -> {
                                    GroupQuestionsDTO groupQuestionsDTO = new GroupQuestionsDTO();
                                    groupQuestionsDTO.setGroupName(g.getName());
                                    groupQuestionsDTO.setQuestions(
                                            g.getQuestions().stream()
                                                    .sorted(Comparator.comparing(QuestionExecutionEntity::getOrderNumber))
                                                    .map(questionConverter::map)
                                                    .collect(toList())
                                    );
                                    return groupQuestionsDTO;
                                }
                        ).collect(toList())
        );

        return dto;
    }
}
