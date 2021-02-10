package com.datapath.checklistapp.service.converter.type;

import com.datapath.checklistapp.util.converter.AnswerFieldType;

public interface AnswerTypeConverter<T> {

    T convert(String value);

    AnswerFieldType type();
}
