package com.datapath.checklistapp.service.converter.type.impl;

import com.datapath.checklistapp.service.converter.type.AnswerTypeConverter;
import com.datapath.checklistapp.util.converter.ValueType;
import org.springframework.stereotype.Component;

@Component
public class AnswerStringConverter implements AnswerTypeConverter<String> {

    @Override
    public String convert(String value) {
        return value;
    }

    @Override
    public ValueType type() {
        return ValueType.STRING;
    }
}
