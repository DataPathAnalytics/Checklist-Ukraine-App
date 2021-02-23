package com.datapath.checklistapp.service.converter.type.impl;

import com.datapath.checklistapp.service.converter.type.AnswerTypeConverter;
import com.datapath.checklistapp.util.converter.ValueType;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static java.util.Objects.isNull;

@Service
public class AnswerDatetimeConverter implements AnswerTypeConverter<LocalDateTime> {

    @Override
    public LocalDateTime convert(String value) {
        if (isNull(value)) return null;
        return LocalDateTime.parse(value);
    }

    @Override
    public ValueType type() {
        return ValueType.DATETIME;
    }
}
