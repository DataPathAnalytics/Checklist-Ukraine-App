package com.datapath.checklistukraineapp.service.converter.type.impl;

import com.datapath.checklistukraineapp.service.converter.type.AnswerTypeConverter;
import com.datapath.checklistukraineapp.util.converter.AnswerFieldType;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;

@Service
public class AnswerIntegerConverter implements AnswerTypeConverter<Integer> {
    @Override
    public Integer convert(String value) {
        if (isNull(value)) return null;
        return Integer.parseInt(value);
    }

    @Override
    public AnswerFieldType type() {
        return AnswerFieldType.INTEGER;
    }
}
