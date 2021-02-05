package com.datapath.checklistukraineapp.service.converter.type;

import com.datapath.checklistukraineapp.util.converter.AnswerFieldType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConvertTypeService {

    private final Map<AnswerFieldType, AnswerTypeConverter> answerTypeConverterMap = new HashMap<>();

    public ConvertTypeService(@Autowired List<AnswerTypeConverter> converters) {
        converters.forEach(c -> answerTypeConverterMap.put(c.type(), c));
    }

    public Object convert(String value, AnswerFieldType type) {
        return answerTypeConverterMap.get(type).convert(value);
    }
}
