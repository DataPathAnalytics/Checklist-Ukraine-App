package com.datapath.checklistapp.service.converter.structure;

import com.datapath.checklistapp.dao.entity.AnswerEntity;
import com.datapath.checklistapp.dao.entity.FieldDescriptionEntity;
import com.datapath.checklistapp.dao.entity.QuestionEntity;
import com.datapath.checklistapp.dao.entity.QuestionExecutionEntity;
import com.datapath.checklistapp.dto.AnswerDTO;
import com.datapath.checklistapp.dto.QuestionDTO;
import com.datapath.checklistapp.dto.QuestionExecutionDTO;
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
        executionDTO.setAnswer(answerConverter.map(answerEntity, entity.getQuestion().getAnswerStructure()));

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
        executionDTO.setParentQuestionId(entity.getParentQuestionId());
        return executionDTO;
    }

    public QuestionDTO map(QuestionEntity entity) {
        QuestionDTO dto = new QuestionDTO();

        BeanUtils.copyProperties(entity, dto);

        //todo:needs solve what to do with knowledge categories
//        dto.setKnowledgeCategoryId(entity.getKnowledgeCategory().getId());
//        dto.setKnowledgeCategoryName(entity.getKnowledgeCategory().getName());

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
