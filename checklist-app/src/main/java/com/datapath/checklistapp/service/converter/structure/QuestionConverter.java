package com.datapath.checklistapp.service.converter.structure;

import com.datapath.checklistapp.dao.entity.*;
import com.datapath.checklistapp.dto.QuestionDTO;
import com.datapath.checklistapp.dto.QuestionExecutionDTO;
import com.datapath.checklistapp.dto.QuestionSourceDTO;
import com.datapath.checklistapp.dto.request.template.CreateTemplateConfigRequest;
import com.datapath.checklistapp.dto.request.template.CreateTemplateRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.springframework.util.CollectionUtils.isEmpty;

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

    public QuestionExecutionEntity map(CreateTemplateRequest.TemplateQuestion q, QuestionEntity question) {
        QuestionExecutionEntity execution = new QuestionExecutionEntity();
        execution.setOrderNumber(q.getOrderNumber());
        execution.setParentQuestionId(q.getParentQuestionId());
        execution.setConditionAnswerId(q.getParentConditionAnswerId());
        execution.setRequired(q.isRequired());
        execution.setLinkType(q.getLinkType());
        execution.setQuestion(question);

        if (!isEmpty(q.getConditionCharacteristics())) {
            execution.setConditionCharacteristics(
                    q.getConditionCharacteristics().stream()
                            .map(c -> new ConditionCharacteristicEntity(
                                    c.isEvaluation(), c.getRiskEventId(), c.getConditionAnswerId()))
                            .collect(toSet())
            );
        }
        return execution;
    }

    public QuestionExecutionEntity map(CreateTemplateConfigRequest.TemplateQuestion q, QuestionEntity question) {
        QuestionExecutionEntity execution = new QuestionExecutionEntity();
        execution.setQuestion(question);
        execution.setParentQuestionId(q.getParentQuestionId());
        execution.setOrderNumber(q.getOrderNumber());
        execution.setRequired(q.isRequired());
        execution.setLinkType(q.getLinkType());
        return execution;
    }
}
