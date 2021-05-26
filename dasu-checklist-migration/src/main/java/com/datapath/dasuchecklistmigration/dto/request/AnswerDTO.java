package com.datapath.dasuchecklistmigration.dto.request;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class AnswerDTO {
    private Integer questionId;
    private String comment;
    private Map<String, Object> values = new HashMap<>();
}
