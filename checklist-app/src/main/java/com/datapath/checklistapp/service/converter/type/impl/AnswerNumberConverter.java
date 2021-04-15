package com.datapath.checklistapp.service.converter.type.impl;

import com.datapath.checklistapp.service.converter.type.AnswerTypeConverter;
import com.datapath.checklistapp.util.converter.ValueType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AnswerNumberConverter implements AnswerTypeConverter<Object> {

    @Override
    public Object convert(String value) {
        if (!StringUtils.hasText(value)) return null;

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return Double.parseDouble(value);
        }
    }

    @Override
    public ValueType type() {
        return ValueType.NUMBER;
    }
}
