package com.datapath.analyticapp.dto.export.question;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class QuestionDTO {
    private Long id;
    private String name;
    private LocalDateTime dateCreated;
    private List<Long> knowledgeCategories;
    private Integer questionTypeId;
    private AnswerStructureDTO answerStructure;

    @Data
    public static class AnswerStructureDTO {
        private Long id;
        private String name;
        private List<FieldDescriptionDTO> fieldDescriptions;
    }

    @Data
    public static class FieldDescriptionDTO {
        private String name;
        private boolean identifier;
    }
}
