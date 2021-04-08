package com.datapath.analyticapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LinkTypeDTO {

    private Long id;
    private String linkTypeName;
    private String linkTypeDescription;
}
