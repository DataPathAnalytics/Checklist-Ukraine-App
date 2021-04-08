package com.datapath.checklistapp.service.converter.structure;

import com.datapath.checklistapp.dao.entity.*;
import com.datapath.checklistapp.dto.AutoCompleteConfigDTO;
import com.datapath.checklistapp.dto.QuestionDTO;
import com.datapath.checklistapp.dto.QuestionExecutionDTO;
import com.datapath.checklistapp.dto.QuestionSourceDTO;
import com.datapath.checklistapp.dto.request.template.CreateTemplateConfigRequest;
import com.datapath.checklistapp.dto.request.template.CreateTemplateRequest;
import com.datapath.checklistapp.exception.EntityNotFoundException;
import com.datapath.checklistapp.util.database.Node;
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
        executionDTO.setLinkTypeId(entity.getLinkTypeId());
        executionDTO.setNodeTypeId(entity.getNodeTypeId());

        executionDTO.setParentConditionAnswerId(entity.getConditionAnswerId());
        executionDTO.setParentQuestionId(entity.getParentQuestionId());

        executionDTO.setQuestion(map(entity.getQuestion()));
        executionDTO.setAnswer(answerConverter.map(answerEntity));

        if (!isEmpty(entity.getAutoCompleteConfig())) {
            executionDTO.setAutoCompleteConfigs(
                    entity.getAutoCompleteConfig().stream()
                            .map(c -> {
                                AutoCompleteConfigDTO dto = new AutoCompleteConfigDTO();
                                dto.setAutoComplete(c.isAutoComplete());
                                dto.setSearchable(c.isSearchable());
                                dto.setDatasource(c.getDatasource());
                                dto.setFieldName(c.getFieldName());
                                dto.setFilterFieldName(c.getFilterFieldName());
                                dto.setFieldId(c.getField().getId());
                                return dto;
                            }).collect(toList())
            );
        }

        return executionDTO;
    }

    public QuestionDTO map(QuestionEntity entity) {
        QuestionDTO dto = new QuestionDTO();

        BeanUtils.copyProperties(entity, dto);

        dto.setKnowledgeCategories(
                entity.getKnowledgeClasses().stream()
                        .mapToLong(KnowledgeClassEntity::getOuterId)
                        .boxed()
                        .collect(toList())
        );

        if (nonNull(entity.getAnswerStructure())) {
            dto.setAnswerStructure(answerConverter.map(entity.getAnswerStructure()));
        }

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
        execution.setLinkTypeId(q.getLinkTypeId());
        execution.setNodeTypeId(q.getNodeTypeId());
        execution.setQuestion(question);

        if (!isEmpty(q.getConditionCharacteristics())) {
            execution.setConditionCharacteristics(
                    q.getConditionCharacteristics().stream()
                            .map(c -> new ConditionCharacteristicEntity(c.getRiskEventTypeId(), c.getConditionAnswerId()))
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
        execution.setLinkTypeId(q.getLinkTypeId());
        execution.setNodeTypeId(q.getNodeTypeId());

        if (!isEmpty(q.getAutoCompleteConfigs())) {
            q.getAutoCompleteConfigs().forEach(config -> {
                AutoCompleteConfigEntity entity = new AutoCompleteConfigEntity();
                entity.setAutoComplete(config.isAutoComplete());
                entity.setSearchable(config.isSearchable());
                entity.setDatasource(config.getDatasource());
                entity.setFieldName(config.getFieldName());
                entity.setFilterFieldName(config.getFilterFieldName());
                entity.setField(
                        question.getAnswerStructure().getFields()
                                .stream()
                                .filter(f -> f.getId().equals(config.getFieldId()))
                                .findFirst().orElseThrow(() -> new EntityNotFoundException(Node.FieldDescription.toString(), config.getFieldId()))
                );
                execution.getAutoCompleteConfig().add(entity);
            });
        }

        return execution;
    }
}
