package com.datapath.checklistapp.service.converter.structure;

import com.datapath.checklistapp.dao.entity.AnswerEntity;
import com.datapath.checklistapp.dao.entity.AnswerStructureEntity;
import com.datapath.checklistapp.dto.AnswerDTO;
import com.datapath.checklistapp.dto.AnswerStructureDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@AllArgsConstructor
public class AnswerConverter {

    private final ObjectMapper mapper;

    private static final TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
    };

    @SneakyThrows
    public AnswerDTO map(AnswerEntity entity) {
        AnswerDTO dto = new AnswerDTO();
        dto.setId(entity.getId());
        dto.setQuestionId(entity.getQuestionExecution().getId());
        dto.setComment(entity.getComment());
        dto.setValues(toValues(entity));

        return dto;
    }

    public AnswerStructureDTO map(AnswerStructureEntity entity) {
        AnswerStructureDTO dto = new AnswerStructureDTO();

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setFieldDescriptions(
                entity.getFields().stream()
                        .map(f -> {
                                    AnswerStructureDTO.FieldDescriptionDTO fieldDTO = new AnswerStructureDTO.FieldDescriptionDTO();
                                    fieldDTO.setId(f.getId());
                                    fieldDTO.setName(f.getName());

                                    if (!isEmpty(f.getValues())) {
                                        fieldDTO.setValues(
                                                f.getValues().stream()
                                                        .map(v -> new AnswerStructureDTO.ValueDTO(v.getId(), v.getValue()))
                                                        .collect(toList())
                                        );
                                    }

                                    fieldDTO.setLabel(f.getLabel());
                                    fieldDTO.setValueType(f.getValueType());
                                    fieldDTO.setComponentType(f.getComponentType());
                                    fieldDTO.setRequired(f.isRequired());
                                    fieldDTO.setTitle(f.isTitle());
                                    fieldDTO.setIdentifier(f.isIdentifier());
                                    return fieldDTO;
                                }
                        ).collect(toList())
        );

        return dto;
    }

    public String toJson(AnswerDTO answer) {
        try {
            return mapper.writeValueAsString(answer.getValues());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, Object> toValues(AnswerEntity entity) {
        try {
            return mapper.readValue(entity.getValues(), typeRef);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
