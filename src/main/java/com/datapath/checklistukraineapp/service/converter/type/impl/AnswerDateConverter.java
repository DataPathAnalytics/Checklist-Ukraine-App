package com.datapath.checklistukraineapp.service.converter.type.impl;

import com.datapath.checklistukraineapp.service.converter.type.AnswerTypeConverter;
import com.datapath.checklistukraineapp.util.converter.AnswerFieldType;
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
    public AnswerFieldType type() {
        return AnswerFieldType.DATE;
    }
}
