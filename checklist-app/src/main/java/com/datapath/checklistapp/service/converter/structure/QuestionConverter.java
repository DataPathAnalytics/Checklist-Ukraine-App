package com.datapath.checklistapp.service.converter.structure;

import com.datapath.checklistapp.dao.entity.*;
import com.datapath.checklistapp.dto.QuestionDTO;
import com.datapath.checklistapp.dto.QuestionExecutionDTO;
import com.datapath.checklistapp.dto.QuestionSourceDTO;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class QuestionConverter {

    private final AnswerConverter answerConverter;

    public QuestionExecutionDTO map(QuestionExecutionEntity entity, AnswerEntity answerEntity) {
        QuestionExecutionDTO executionDTO = new QuestionExecutionDTO();
        executionDTO.setId(entity.getId());
        executionDTO.setRequired(entity.isRequired());
        executionDTO.setLinkType(entity.getLinkType());

        executionDTO.setParentConditionAnswerId(entity.getConditionAnswerId());
        executionDTO.setParentQuestionId(entity.getParentQuestionId());

        executionDTO.setQuestion(map(entity.getQuestion()));
        executionDTO.setAnswer(answerConverter.map(answerEntity));

        return executionDTO;
    }

    public QuestionDTO map(QuestionEntity entity) {
        QuestionDTO dto = new QuestionDTO();

        BeanUtils.copyProperties(entity, dto);

        dto.setKnowledgeCategories(
                entity.getKnowledgeCategory().stream()
                        .mapToLong(KnowledgeCategoryEntity::getOuterId)
                        .boxed()
                        .collect(toList())
        );

        if (nonNull(entity.getSource())) {
            QuestionSourceDTO source = this.map(entity.getSource().getSource());
            source.setParagraph(entity.getSource().getDocumentParagraph());
        }

        if (nonNull(entity.getAnswerStructure())) {
            dto.setAnswerStructure(answerConverter.map(entity.getAnswerStructure()));
        }

        dto.setLinkTypes(entity.getLinkTypes().stream().map(LinkTypeEntity::getLinkType).collect(toList()));

        return dto;
    }

    public QuestionExecutionDTO map(QuestionExecutionEntity entity) {
        return this.map(entity, null);
    }

    public QuestionSourceDTO map(QuestionSourceEntity entity) {
        QuestionSourceDTO source = new QuestionSourceDTO();
        source.setId(entity.getId());
        source.setName(entity.getName());
        source.setLink(entity.getLink());
        return source;
    }
}
