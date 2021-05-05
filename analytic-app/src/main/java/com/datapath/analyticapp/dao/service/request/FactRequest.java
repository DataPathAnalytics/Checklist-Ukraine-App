package com.datapath.analyticapp.dao.service.request;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class FactRequest {
    private Long parentId;
    private String fieldName;
    private String questionValue;
    private String nodeType;
    private String linkType;
    private Map<String, Object> props;
    private Long initiatorId;
}
