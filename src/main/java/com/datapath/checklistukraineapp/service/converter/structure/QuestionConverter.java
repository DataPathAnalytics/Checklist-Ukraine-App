package com.datapath.checklistukraineapp.service.converter.structure;

import com.datapath.checklistukraineapp.dao.entity.AnswerEntity;
import com.datapath.checklistukraineapp.dao.entity.FieldDescriptionEntity;
import com.datapath.checklistukraineapp.dao.entity.QuestionEntity;
import com.datapath.checklistukraineapp.dao.entity.QuestionExecutionEntity;
import com.datapath.checklistukraineapp.dto.AnswerDTO;
import com.datapath.checklistukraineapp.dto.QuestionDTO;
import com.datapath.checklistukraineapp.dto.QuestionExecutionDTO;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toMap;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@AllArgsConstructor
public class QuestionConverter {

    private final AnswerConverter answerConverter;

    public QuestionExecutionDTO map(QuestionExecutionEntity entity, AnswerEntity answerEntity) {
        QuestionExecutionDTO executionDTO = new QuestionExecutionDTO();
        executionDTO.setQuestion(map(entity.getQuestion()));
        executionDTO.setAnswer(answerConverter.map(answerEntity));

        if (isNull(executionDTO.getAnswer())) {
            Map<String, Object> defaultAnswerFields = entity.getQuestion().getAnswerStructure()
                    .getFields()
                    .stream()
                    .filter(s -> nonNull(s.getDefaultValue()))
                    .collect(toMap(FieldDescriptionEntity::getName, FieldDescriptionEntity::getDefaultValue));
            if (!isEmpty(defaultAnswerFields)) {
                AnswerDTO answerDTO = new AnswerDTO();
                answerDTO.setValues(defaultAnswerFields);
                executionDTO.setAnswer(answerDTO);
            }
        }

        executionDTO.setConditionAnswerId(entity.getConditionAnswerId());
        executionDTO.setId(entity.getId());
        executionDTO.setParentFeatureId(entity.getParentFeatureId());
        executionDTO.setParentQuestionId(entity.getParentQuestionId());
        return executionDTO;
    }

    public QuestionDTO map(QuestionEntity entity) {
        QuestionDTO dto = new QuestionDTO();

        BeanUtils.copyProperties(entity, dto);

        dto.setKnowledgeCategoryId(entity.getKnowledgeCategory().getId());
        dto.setKnowledgeCategoryValue(entity.getKnowledgeCategory().getValue());
        dto.setKnowledgeCategoryTranslate(entity.getKnowledgeCategory().getTranslate());

        dto.setQuestionTypeId(entity.getType().getQuestionTypeId());
        dto.setQuestionTypeValue(entity.getType().getValue());

        dto.setQuestionSourceId(entity.getSource().getSource().getId());
        dto.setQuestionSourceName(entity.getSource().getSource().getName());
        dto.setQuestionSourceLink(entity.getSource().getSource().getLink());
        dto.setDocumentParagraph(entity.getSource().getDocumentParagraph());

        if (nonNull(entity.getAnswerStructure())) {
            dto.setAnswerStructure(answerConverter.map(entity.getAnswerStructure()));
        }

        return dto;
    }

    public QuestionExecutionDTO map(QuestionExecutionEntity entity) {
        QuestionExecutionDTO dto = new QuestionExecutionDTO();

        BeanUtils.copyProperties(entity, dto);
        dto.setQuestion(map(entity.getQuestion()));

        return dto;
    }
}
