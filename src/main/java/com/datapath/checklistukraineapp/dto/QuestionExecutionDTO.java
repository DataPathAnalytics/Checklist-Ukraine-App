package com.datapath.checklistukraineapp.dto;

import lombok.Data;

@Data
public class QuestionExecutionDTO {

    private Long id;
    private Long parentQuestionId;
    private Integer conditionAnswerId;
    private Long parentFeatureId;
    private QuestionDTO question;
    private AnswerDTO answer;
}
