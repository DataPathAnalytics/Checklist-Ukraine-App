package com.datapath.checklistapp.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuestionDTO {

    private Long id;
    private String name;
    private LocalDateTime dateCreated;

    private Long knowledgeCategoryId;
    private String knowledgeCategoryName;

    private String questionSourceId;
    private String questionSourceName;
    private String questionSourceLink;
    private String documentParagraph;

    private AnswerStructureDTO answerStructure;
    private Integer questionTypeId;
    private String questionTypeValue;
}
