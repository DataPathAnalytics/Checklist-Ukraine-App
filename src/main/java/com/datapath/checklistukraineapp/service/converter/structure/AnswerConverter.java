package com.datapath.checklistukraineapp.service.converter.structure;

import com.datapath.checklistukraineapp.dao.entity.AnswerEntity;
import com.datapath.checklistukraineapp.dao.entity.AnswerStructureEntity;
import com.datapath.checklistukraineapp.dto.AnswerDTO;
import com.datapath.checklistukraineapp.dto.AnswerStructureDTO;
import com.datapath.checklistukraineapp.service.converter.type.ConvertTypeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class AnswerConverter {

    private final ConvertTypeService convertTypeService;

    public AnswerDTO map(AnswerEntity entity) {
        if (isNull(entity)) return null;

        AnswerDTO dto = new AnswerDTO();

        if (nonNull(entity.getAnswerType())) {
            dto.setAnswerTypeId(entity.getAnswerType().getAnswerTypeId());
        }
        dto.setComment(entity.getComment());
        dto.setValues(entity.getValues());

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
