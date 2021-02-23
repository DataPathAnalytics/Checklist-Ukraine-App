package com.datapath.checklistapp.service.converter.type.impl;

import com.datapath.checklistapp.service.converter.type.AnswerTypeConverter;
import com.datapath.checklistapp.util.converter.ValueType;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static java.util.Objects.isNull;

@Service
public class AnswerDateConverter implements AnswerTypeConverter<LocalDate> {

    @Override
    public LocalDate convert(String value) {
        if (isNull(value)) return null;
        return LocalDate.parse(value);
    }

    @Override
    public ValueType type() {
        return ValueType.DATE;
    }
}
