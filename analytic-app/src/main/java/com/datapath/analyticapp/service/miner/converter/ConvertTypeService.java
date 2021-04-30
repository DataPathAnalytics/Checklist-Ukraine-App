package com.datapath.analyticapp.service.miner.converter;

import com.datapath.analyticapp.dto.imported.response.ValueType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConvertTypeService {

    private final Map<ValueType, AnswerTypeConverter> answerTypeConverterMap = new HashMap<>();

    public ConvertTypeService(@Autowired List<AnswerTypeConverter> converters) {
        converters.forEach(c -> answerTypeConverterMap.put(c.type(), c));
    }

    public Object convert(Object value, ValueType type) {
        return answerTypeConverterMap.get(type).convert(value);
    }
}
