package com.datapath.checklistapp.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class QuestionExecutionDTO {

    private Long id;
    private Long parentQuestionId;
    private Integer parentConditionAnswerId;
    private QuestionDTO question;
    private AnswerDTO answer;
    private boolean required;
    private Long linkTypeId;
    private Long nodeTypeId;

    private List<AutoCompleteConfigDTO> autoCompleteConfigs = new ArrayList<>();
}
