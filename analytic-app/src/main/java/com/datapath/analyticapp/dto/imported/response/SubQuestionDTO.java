package com.datapath.analyticapp.dto.imported.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class SubQuestionDTO {
    private String conditionFieldName;
    private Map<Long, List<Long>> conditionAnswerQuestionIds = new HashMap<>();
    private List<Long> questionIds = new ArrayList<>();

}
