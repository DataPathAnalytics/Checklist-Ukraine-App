package com.datapath.checklistapp.service.mapper;

import com.datapath.checklistapp.dao.entity.*;
import com.datapath.checklistapp.dto.*;
import com.datapath.checklistapp.dto.request.template.SaveTemplateConfigRequest.TemplateConfigQuestion;
import com.datapath.checklistapp.dto.request.template.SaveTemplateRequest.TemplateQuestion;
import com.datapath.checklistapp.util.database.QuestionExecutionRole;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@AllArgsConstructor
public class QuestionMapper {

    private final AnswerMapper answerMapper;

    public QuestionExecutionDTO map(QuestionExecutionEntity entity) {
        QuestionExecutionDTO executionDTO = new QuestionExecutionDTO();
        executionDTO.setId(entity.getId());
        executionDTO.setRequired(entity.isRequired());
        executionDTO.setRoot(entity.isRoot());
        executionDTO.setLinkType(entity.getLinkType());
        executionDTO.setNodeType(entity.getNodeType());
        executionDTO.setOrderNumber(entity.getOrderNumber());
        executionDTO.setMiningRoleId(entity.getMiningRoleId());

        executionDTO.setQuestion(map(entity.getQuestion()));

        executionDTO.setParentQuestionId(entity.getParentQuestionId());
        executionDTO.setParentConditionFieldName(entity.getConditionFieldName());
        executionDTO.setParentConditionAnswerId(entity.getConditionAnswerId());

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
                                dto.setFieldId(c.getFieldId());
                                return dto;
                            }).collect(toList())
            );
        }

        if (!isEmpty(entity.getConditionCharacteristics())) {
            executionDTO.setConditionCharacteristics(
                    entity.getConditionCharacteristics()
                            .stream()
                            .map(this::map)
                            .collect(toList())
            );
        }

        return executionDTO;
    }

    public QuestionDTO map(QuestionEntity entity) {
        QuestionDTO dto = new QuestionDTO();

        BeanUtils.copyProperties(entity, dto);

        dto.setKnowledgeClasses(entity.getKnowledgeClasses());

        if (nonNull(entity.getAnswerStructure())) {
            dto.setAnswerStructure(answerMapper.map(entity.getAnswerStructure()));
        }

        if (nonNull(entity.getQuestionSource())) {
            dto.setSource(map(entity.getQuestionSource()));
        }

        return dto;
    }

    public QuestionSourceDTO map(QuestionSourceEntity entity) {
        QuestionSourceDTO source = new QuestionSourceDTO();
        source.setIdentifier(entity.getIdentifier());
        source.setName(entity.getName());
        source.setLink(entity.getLink());
        return source;
    }

    public QuestionExecutionEntity map(TemplateQuestion q, QuestionEntity question) {
        QuestionExecutionEntity execution = new QuestionExecutionEntity();
        execution.setOrderNumber(q.getOrderNumber());
        execution.setRequired(q.isRequired());
        execution.setLinkType(q.getLinkType());
        execution.setNodeType(q.getNodeType());
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

    public QuestionExecutionEntity map(TemplateConfigQuestion q, QuestionEntity question, QuestionExecutionRole role) {
        QuestionExecutionEntity execution = new QuestionExecutionEntity();
        execution.setQuestion(question);
        execution.setOrderNumber(q.getOrderNumber());
        execution.setRequired(q.isRequired());
        execution.setLinkType(q.getLinkType());
        execution.setNodeType(q.getNodeType());
        execution.setMiningRoleId(q.getMiningRoleId());
        execution.setRole(role);

        if (!isEmpty(q.getAutoCompleteConfigs())) {
            q.getAutoCompleteConfigs().forEach(config -> {
                AutoCompleteConfigEntity entity = new AutoCompleteConfigEntity();
                entity.setAutoComplete(config.isAutoComplete());
                entity.setSearchable(config.isSearchable());
                entity.setDatasource(config.getDatasource());
                entity.setFieldName(config.getFieldName());
                entity.setFilterFieldName(config.getFilterFieldName());
                entity.setFieldId(config.getFieldId());
                execution.getAutoCompleteConfig().add(entity);
            });
        }

        return execution;
    }

    private ConditionCharacteristicDTO map(ConditionCharacteristicEntity entity) {
        return new ConditionCharacteristicDTO(entity.getOuterRiskEventTypeId(), entity.getConditionAnswerId());
    }
}
