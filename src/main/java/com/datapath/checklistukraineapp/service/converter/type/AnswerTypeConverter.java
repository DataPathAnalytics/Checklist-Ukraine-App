package com.datapath.checklistukraineapp.service.converter.type;

import com.datapath.checklistukraineapp.util.converter.AnswerFieldType;

public interface AnswerTypeConverter<T> {

    T convert(String value);

    AnswerFieldType type();
}
