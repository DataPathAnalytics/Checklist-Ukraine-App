package com.datapath.analyticapp.service.miner.converter;

public interface AnswerTypeConverter<T> {

    T convert(Object value);

    ValueType type();
}
