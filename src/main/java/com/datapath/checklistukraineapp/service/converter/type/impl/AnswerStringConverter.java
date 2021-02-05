package com.datapath.checklistukraineapp.service.converter.type.impl;

import com.datapath.checklistukraineapp.service.converter.type.AnswerTypeConverter;
import com.datapath.checklistukraineapp.util.converter.AnswerFieldType;
import org.springframework.stereotype.Component;

@Component
public class AnswerStringConverter implements AnswerTypeConverter<String> {

    @Override
    public String convert(String value) {
        return value;
    }

    @Override
    public AnswerFieldType type() {
        return AnswerFieldType.STRING;
    }
}
