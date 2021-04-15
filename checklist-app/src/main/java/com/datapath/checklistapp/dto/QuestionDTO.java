package com.datapath.checklistapp.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class QuestionDTO {

    private Long id;
    private String value;
    private LocalDateTime dateCreated;

    private List<Long> knowledgeCategories;

    private QuestionSourceDTO source;

    private AnswerStructureDTO answerStructure;
}
