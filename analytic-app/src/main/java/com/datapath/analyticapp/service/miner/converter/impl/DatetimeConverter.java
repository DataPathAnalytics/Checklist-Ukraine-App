package com.datapath.analyticapp.service.miner.converter.impl;

import com.datapath.analyticapp.dto.imported.response.ValueType;
import com.datapath.analyticapp.service.miner.converter.AnswerTypeConverter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static java.util.Objects.isNull;

@Service
public class DatetimeConverter implements AnswerTypeConverter<LocalDateTime> {

    @Override
    public LocalDateTime convert(Object value) {
        if (isNull(value)) return null;
        return LocalDateTime.parse(value.toString());
    }

    @Override
    public ValueType type() {
        return ValueType.DATETIME;
    }
}
