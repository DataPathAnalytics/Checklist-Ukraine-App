package com.datapath.checklistapp.dto;

import lombok.Data;

@Data
public class AutoCompleteConfigDTO {
    private boolean autoComplete;
    private boolean searchable;
    private String datasource;
    private String fieldName;
    private String filterFieldName;
    private Integer fieldId;
}
