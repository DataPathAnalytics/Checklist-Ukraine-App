package com.datapath.analyticapp.service.miner;

import com.datapath.analyticapp.dto.imported.response.ValueType;

public class ValueTypeConverter {
    public static String convert(Object identifierValue, ValueType identifierType) {
        if (identifierType.equals(ValueType.STRING)) return "'" + identifierValue.toString() + "'";

        return identifierValue.toString();
    }
}
