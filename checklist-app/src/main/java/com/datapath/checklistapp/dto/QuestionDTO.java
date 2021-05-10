package com.datapath.checklistapp.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class QuestionDTO {

    private Integer id;
    private String value;
    private LocalDateTime dateCreated;

    private Set<Long> knowledgeClasses;

    private QuestionSourceDTO source;

    private AnswerStructureDTO answerStructure;
}
