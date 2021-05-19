package com.datapath.analyticapp.service.miner.converter.impl;

import com.datapath.analyticapp.service.miner.converter.AnswerTypeConverter;
import com.datapath.analyticapp.service.miner.converter.ValueType;
import org.springframework.stereotype.Component;

@Component
public class StringConverter implements AnswerTypeConverter<String> {

    @Override
    public String convert(Object value) {
        return value.toString();
    }

    @Override
    public ValueType type() {
        return ValueType.STRING;
    }
}
