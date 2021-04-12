package com.datapath.checklistapp.service.converter.structure;

import com.datapath.checklistapp.dao.entity.AnswerEntity;
import com.datapath.checklistapp.dao.entity.AnswerStructureEntity;
import com.datapath.checklistapp.dto.AnswerDTO;
import com.datapath.checklistapp.dto.AnswerStructureDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
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
        dto.setComment(entity.getComment());

        if (nonNull(entity.getValue())) {
            dto.setValueId(entity.getValue().getId());
        } else {
            dto.setValues(mapper.readValue(entity.getJsonValues(), typeRef));
        }

        return dto;
    }

    @SneakyThrows
    public AnswerDTO mapOnlyFilterFields(AnswerEntity entity, Set<String> filterFieldNames) {
        AnswerDTO dto = new AnswerDTO();

        dto.setValues(
                mapper.readValue(entity.getJsonValues(), typeRef).entrySet()
                        .stream()
                        .filter(e -> filterFieldNames.contains(e.getKey()))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
        );

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
}
