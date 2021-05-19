package com.datapath.analyticapp.service.miner.converter.impl;

import com.datapath.analyticapp.service.miner.converter.AnswerTypeConverter;
import com.datapath.analyticapp.service.miner.converter.ValueType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class NumberConverter implements AnswerTypeConverter<Object> {

    @Override
    public Object convert(Object value) {
        if (!StringUtils.hasText(value.toString())) return null;

        try {
            return Long.parseLong(value.toString());
        } catch (NumberFormatException e) {
            return Double.parseDouble(value.toString());
        }
    }

    @Override
    public ValueType type() {
        return ValueType.NUMBER;
    }
}
