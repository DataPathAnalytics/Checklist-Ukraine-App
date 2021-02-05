package com.datapath.checklistukraineapp.dto;

import com.datapath.checklistukraineapp.util.converter.AnswerFieldType;
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
        private Object defaultValue;
        private String description;
        private String dataSource;
        private AnswerFieldType type;
        private boolean title;
        private boolean required;
    }
}
