package com.datapath.analyticapp.dto.imported.response;

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
}
