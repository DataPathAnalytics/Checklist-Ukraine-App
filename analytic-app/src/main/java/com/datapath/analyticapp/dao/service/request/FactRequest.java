package com.datapath.analyticapp.dao.service.request;

import com.datapath.analyticapp.dto.imported.response.ValueType;
import com.datapath.analyticapp.service.miner.ValueTypeConverter;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FactRequest {
    private Long parentId;
    private Object value;
    private String valueName;
    private ValueType valueType;
    private String questionValue;
    private String nodeType;
    private String linkType;

    public String getValue() {
        return ValueTypeConverter.convert(value, valueType);
    }
}
