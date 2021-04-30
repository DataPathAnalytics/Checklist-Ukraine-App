package com.datapath.analyticapp.dao.service.request;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class IdentifierRequest {
    private String nodeType;
    private String identifierField;
    private Object identifierValue;
    private Map<String, Object> props;
}
