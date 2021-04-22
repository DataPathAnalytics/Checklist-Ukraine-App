package com.datapath.analyticapp.dto.imported.response;

import lombok.Data;

import java.util.Map;

@Data
public class AnswerDTO {
    private Long questionId;
    private Map<String, Object> values;
}
