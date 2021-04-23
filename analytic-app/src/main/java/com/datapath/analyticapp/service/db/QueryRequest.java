package com.datapath.analyticapp.service.db;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class QueryRequest {

    private Long id;
    private String nodeType;
    private String addNodeType;
    private String relType;
    private String identifierField;
    private Object identifierValue;
    private Map<String, Object> params;
}
