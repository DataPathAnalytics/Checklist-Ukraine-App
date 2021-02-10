package com.datapath.checklistapp.dto;

import lombok.Data;

@Data
public class QuestionExecutionDTO {

    private Long id;
    private Long parentQuestionId;
    private Integer conditionAnswerId;
    private QuestionDTO question;
    private AnswerDTO answer;
}
