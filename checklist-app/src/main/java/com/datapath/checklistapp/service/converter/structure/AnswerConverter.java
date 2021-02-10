package com.datapath.checklistapp.service.converter.structure;

import com.datapath.checklistapp.dao.entity.AnswerEntity;
import com.datapath.checklistapp.dao.entity.AnswerStructureEntity;
import com.datapath.checklistapp.dto.AnswerDTO;
import com.datapath.checklistapp.dto.AnswerStructureDTO;
import com.datapath.checklistapp.service.converter.type.ConvertTypeService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class AnswerConverter {

    private final ConvertTypeService convertTypeService;
    private final ObjectMapper mapper;

    private static final TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
    };

    @SneakyThrows
    public AnswerDTO map(AnswerEntity entity, AnswerStructureEntity answerStructureEntity) {
        if (isNull(entity)) return null;

        AnswerDTO dto = new AnswerDTO();
        dto.setComment(entity.getComment());

        if (nonNull(entity.getAnswerType())) {
            dto.setAnswerTypeId(entity.getAnswerType().getAnswerTypeId());
        } else {
            Map<String, Object> answerValues = mapper.readValue(entity.getJsonValues(), typeRef);

            answerStructureEntity.getFields().forEach(f -> {
                if (nonNull(f.getDefaultValue()) && !answerValues.containsKey(f.getName())) {
                    answerValues.put(f.getName(), convertTypeService.convert(f.getDefaultValue(), f.getType()));
                }
            });

            dto.setValues(answerValues);
        }

        return dto;
    }

    public AnswerStructureDTO map(AnswerStructureEntity entity) {
        AnswerStructureDTO dto = new AnswerStructureDTO();

        dto.setId(entity.getId());
        dto.setLinkType(entity.getLinkType());
        dto.setName(entity.getName());
        dto.setFieldDescriptions(
                entity.getFields().stream()
                        .map(f -> {
                                    AnswerStructureDTO.FieldDescriptionDTO fieldDTO = new AnswerStructureDTO.FieldDescriptionDTO();
                                    fieldDTO.setName(f.getName());
                                    fieldDTO.setDefaultValue(convertTypeService.convert(f.getDefaultValue(), f.getType()));
                                    fieldDTO.setDescription(f.getDescription());
                                    fieldDTO.setDataSource(f.getDataSource());
                                    fieldDTO.setType(f.getType());
                                    fieldDTO.setRequired(f.isRequired());
                                    fieldDTO.setTitle(f.isTitle());
                                    return fieldDTO;
                                }
                        ).collect(toList())
        );

        return dto;
    }
}
