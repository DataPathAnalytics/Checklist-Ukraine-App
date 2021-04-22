package com.datapath.analyticapp.dto.imported.response;

import lombok.Data;

@Data
public class FieldDescriptionDTO {
    private Long id;
    private String name;
    private String valueType;
    private boolean identifier;
}
