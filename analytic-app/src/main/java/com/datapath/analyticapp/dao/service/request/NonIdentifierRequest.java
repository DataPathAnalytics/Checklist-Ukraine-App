package com.datapath.analyticapp.dao.service.request;

import com.datapath.analyticapp.dto.imported.response.ValueType;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class NonIdentifierRequest {
    private Long parentId;
    private String nodeType;
    private String linkType;
    private Map<String, Object> props;
    private Map<String, ValueType> fieldTypes;
}
