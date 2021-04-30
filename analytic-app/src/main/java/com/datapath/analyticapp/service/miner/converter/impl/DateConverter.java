package com.datapath.analyticapp.service.miner.converter.impl;

import com.datapath.analyticapp.dto.imported.response.ValueType;
import com.datapath.analyticapp.service.miner.converter.AnswerTypeConverter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static java.util.Objects.isNull;

@Service
public class DateConverter implements AnswerTypeConverter<LocalDate> {

    @Override
    public LocalDate convert(Object value) {
        if (isNull(value)) return null;
        return LocalDate.parse(value.toString());
    }

    @Override
    public ValueType type() {
        return ValueType.DATE;
    }
}
