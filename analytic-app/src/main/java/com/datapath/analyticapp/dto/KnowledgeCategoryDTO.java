package com.datapath.analyticapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KnowledgeCategoryDTO {

    private Long id;
    private String value;
    private String name;
}
