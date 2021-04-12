package com.datapath.checklistapp.dto;

import lombok.Data;

import java.util.List;

@Data
public class GroupQuestionsDTO {

    private String groupName;
    private Integer orderNumber;
    private List<QuestionExecutionDTO> questions;
}
