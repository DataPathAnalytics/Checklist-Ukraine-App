package com.datapath.checklistapp.service.converter.type.impl;

import com.datapath.checklistapp.service.converter.type.AnswerTypeConverter;
import com.datapath.checklistapp.util.converter.ValueType;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;

@Service
public class AnswerDoubleConverter implements AnswerTypeConverter<Double> {

    @Override
    public Double convert(String value) {
        if (isNull(value)) return null;
        return Double.parseDouble(value);
    }

    @Override
    public ValueType type() {
        return ValueType.DOUBLE;
    }
}
