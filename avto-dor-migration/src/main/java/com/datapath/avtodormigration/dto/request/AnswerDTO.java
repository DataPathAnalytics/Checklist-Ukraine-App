package com.datapath.avtodormigration.dto.request;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class AnswerDTO {
    private Integer questionId;
    private Map<String, Object> values = new HashMap<>();
}
