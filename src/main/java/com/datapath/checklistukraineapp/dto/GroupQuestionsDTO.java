package com.datapath.checklistukraineapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GroupQuestionsDTO {

    private String groupName;
    private List<TemplateQuestionDTO> questions;
}
