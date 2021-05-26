package com.datapath.dasuchecklistmigration.dto.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class SubQuestionDTO {
    private Map<String, List<Integer>> conditionAnswerQuestionIds = new HashMap<>();
    private List<Integer> questionIds = new ArrayList<>();
}
