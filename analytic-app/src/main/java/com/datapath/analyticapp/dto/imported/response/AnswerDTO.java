package com.datapath.analyticapp.dto.imported.response;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class AnswerDTO {
    private Long id;
    private Long questionId;
    private Map<String, Object> values = new HashMap<>();
}
