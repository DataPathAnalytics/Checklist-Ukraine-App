package com.datapath.analyticapp.dto.imported.response;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SubQuestionDTO {
    private String conditionFieldName;
    private Map<Long, List<Long>> conditionAnswerQuestionIds;
    private List<Long> questionIds;

}
