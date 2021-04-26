package com.datapath.analyticapp.dto.imported.response;

import lombok.Data;

import java.util.List;

@Data
public class QuestionExecutionDTO {
    private Long id;
    private boolean root;
    private Long linkTypeId;
    private Long nodeTypeId;
    private Long roleId;
    private QuestionDTO question;
    private List<SubQuestionDTO> subQuestions;
    private List<ConditionCharacteristicDTO> conditionCharacteristics;
}
