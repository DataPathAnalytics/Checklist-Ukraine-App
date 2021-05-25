package com.datapath.analyticapp.dto.imported.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class QuestionDTO {
    private Long id;
    private String value;
    private LocalDateTime dateCreated;
    private List<Long> knowledgeClasses = new ArrayList<>();
    private AnswerStructureDTO answerStructure;
}
