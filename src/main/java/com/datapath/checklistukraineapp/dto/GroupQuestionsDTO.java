package com.datapath.checklistukraineapp.dto;

import lombok.Data;

import java.util.List;

@Data
public class GroupQuestionsDTO {

    private String groupName;
    private List<QuestionExecutionDTO> questions;
}
