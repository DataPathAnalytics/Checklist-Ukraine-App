package com.datapath.checklistukraineapp.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class QuestionDTO {

    private Long id;
    private String name;
    private LocalDate dateCreated;

    private Integer knowledgeCategoryId;
    private String knowledgeCategoryValue;
    private String knowledgeCategoryTranslate;

    private String questionSourceId;
    private String questionSourceName;
    private String questionSourceLink;
    private String documentParagraph;

    private Long answerStructureId;
    private Integer questionTypeId;
    private String questionTypeValue;
}
