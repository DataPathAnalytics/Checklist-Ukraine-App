package com.datapath.checklistapp.service.converter.type;

import com.datapath.checklistapp.util.converter.ValueType;
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

    public Object convert(String value, ValueType type) {
        return answerTypeConverterMap.get(type).convert(value);
    }
}
