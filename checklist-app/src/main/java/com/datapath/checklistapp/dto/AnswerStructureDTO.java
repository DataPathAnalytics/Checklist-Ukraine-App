package com.datapath.checklistapp.dto;

import com.datapath.checklistapp.util.converter.ComponentType;
import com.datapath.checklistapp.util.converter.ValueType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class AnswerStructureDTO {

    private Integer id;
    private String name;
    private String linkType;
    private List<FieldDescriptionDTO> fieldDescriptions;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FieldDescriptionDTO {
        private Integer id;
        private String name;
        private List<ValueDTO> values;
        private String label;
        private ValueType valueType;
        private ComponentType componentType;
        private boolean title;
        private boolean required;
        private boolean identifier;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ValueDTO {
        private Integer id;
        private String value;
    }
}
