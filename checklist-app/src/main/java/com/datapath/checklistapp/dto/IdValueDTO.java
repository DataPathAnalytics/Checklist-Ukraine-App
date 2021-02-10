package com.datapath.checklistapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IdValueDTO {

    private Object id;
    private String value;
    private String translate;

    public IdValueDTO(Object id, String value) {
        this.id = id;
        this.value = value;
    }
}
