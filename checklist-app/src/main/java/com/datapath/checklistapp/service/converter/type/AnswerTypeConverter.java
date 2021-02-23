package com.datapath.checklistapp.service.converter.type;

import com.datapath.checklistapp.util.converter.ValueType;

public interface AnswerTypeConverter<T> {

    T convert(String value);

    ValueType type();
}
