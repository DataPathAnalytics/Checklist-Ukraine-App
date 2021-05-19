package com.datapath.analyticapp.dto.imported.response;

import com.datapath.analyticapp.service.miner.converter.ValueType;
import lombok.Data;

import java.util.List;

@Data
public class FieldDescriptionDTO {
    private Long id;
    private String name;
    private ValueType valueType;
    private List<ValueDTO> values;
    private boolean identifier;

    @Data
    public static class ValueDTO {
        private Long id;
        private String value;
    }
}
