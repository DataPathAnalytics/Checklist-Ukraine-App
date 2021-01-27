package com.datapath.checklistukraineapp.dto;

import com.datapath.checklistukraineapp.util.AnswerFieldType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class AnswerStructureDTO {

    private Long id;
    private String name;
    private String linkType;
    private List<FieldDescriptionDTO> fieldDescriptions;

    @Data
    @AllArgsConstructor
    public static class FieldDescriptionDTO {
        private String name;
        private String description;
        private String dataSource;
        private AnswerFieldType type;
    }
}
