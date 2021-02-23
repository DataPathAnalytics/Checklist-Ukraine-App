package com.datapath.checklistapp.dto;

import com.datapath.checklistapp.util.converter.ComponentType;
import com.datapath.checklistapp.util.converter.ValueType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class AnswerStructureDTO {

    private Long id;
    private String name;
    private String linkType;
    private List<FieldDescriptionDTO> fieldDescriptions;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FieldDescriptionDTO {
        private String name;
        private List<Object> defaultValues;
        private String description;
        private String dataSource;
        private ValueType valueType;
        private ComponentType componentType;
        private boolean title;
        private boolean required;
    }
}
