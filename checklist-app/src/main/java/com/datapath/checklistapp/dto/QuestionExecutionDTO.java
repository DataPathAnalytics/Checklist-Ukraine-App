package com.datapath.checklistapp.dto;

import lombok.Data;

@Data
public class QuestionExecutionDTO {

    private Long id;
    private Long parentQuestionId;
    private Integer parentConditionAnswerId;
    private QuestionDTO question;
    private AnswerDTO answer;
    private boolean required;
}
