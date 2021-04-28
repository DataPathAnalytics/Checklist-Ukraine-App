package com.datapath.analyticapp.dao.service.request;

import com.datapath.analyticapp.dto.imported.response.ValueType;
import com.datapath.analyticapp.service.miner.ValueTypeConverter;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class IdentifierRequest {
    private String nodeType;
    private String identifierField;
    private Object identifierValue;
    private ValueType identifierType;
    private Map<String, Object> props;

    public String getIdentifierValue() {
        return ValueTypeConverter.convert(identifierValue, identifierType);
    }
}
