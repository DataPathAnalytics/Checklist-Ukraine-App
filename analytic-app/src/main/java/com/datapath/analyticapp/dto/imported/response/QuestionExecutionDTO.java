package com.datapath.analyticapp.dto.imported.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class QuestionExecutionDTO {
    private Long id;
    private boolean root;
    private String nodeType;
    private String linkType;
    private Long roleId;
    private QuestionDTO question;
    private List<SubQuestionDTO> subQuestions = new ArrayList<>();
    private List<ConditionCharacteristicDTO> conditionCharacteristics = new ArrayList<>();
}
