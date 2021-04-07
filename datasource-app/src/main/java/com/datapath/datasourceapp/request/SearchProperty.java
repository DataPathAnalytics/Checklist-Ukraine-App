package com.datapath.datasourceapp.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchProperty {

    @NotBlank
    private String datasource;
    @NotBlank
    private String fieldName;
    private Object fieldValue = "";

    private String filterFieldName;
    private String filterFieldValue = "";
}
