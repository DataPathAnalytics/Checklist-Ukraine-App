package com.datapath.analyticapp.service.db;

import com.datapath.analyticapp.dto.imported.response.ValueType;
import com.datapath.analyticapp.service.miner.ValueTypeConverter;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class QueryRequest {

    private Long parentId;
    private Long childId;
    private String parentNodeType;
    private String childNodeType;
    private String relType;
    private String identifierField;
    private Object identifierValue;
    private ValueType identifierType;
    private Map<String, Object> params;

    public String getIdentifierValue() {
        return ValueTypeConverter.convert(identifierValue, identifierType);
    }
}
