package com.datapath.analyticapp.service.miner.converter;

import com.datapath.analyticapp.dto.imported.response.ValueType;

public interface AnswerTypeConverter<T> {

    T convert(Object value);

    ValueType type();
}
