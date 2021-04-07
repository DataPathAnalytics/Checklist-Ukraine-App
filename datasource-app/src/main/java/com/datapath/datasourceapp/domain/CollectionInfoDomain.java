package com.datapath.datasourceapp.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CollectionInfoDomain {

    private String name;
    private String description;
    private List<FieldDomain> fields;
}
