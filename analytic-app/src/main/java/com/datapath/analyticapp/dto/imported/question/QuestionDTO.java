package com.datapath.analyticapp.dto.imported.question;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class QuestionDTO {
    private Long id;
    private String value;
    private LocalDateTime dateCreated;
    private List<Long> knowledgeClasses;
    private AnswerStructureDTO answerStructure;

    @Data
    public static class AnswerStructureDTO {
        private Long id;
        private String name;
        private List<FieldDescriptionDTO> fieldDescriptions;
    }

    @Data
    public static class FieldDescriptionDTO {
        private Long id;
        private String name;
        private String valueType;
        private boolean identifier;

        private List<ValueDTO> values;
    }

    @Data
    public static class ValueDTO {
        private Long id;
        private String value;
    }
}
