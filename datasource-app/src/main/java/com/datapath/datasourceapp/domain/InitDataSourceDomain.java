package com.datapath.datasourceapp.domain;

import lombok.Data;
import org.bson.Document;

import java.util.List;

@Data
public class InitDataSourceDomain {

    private String name;
    private String description;
    private String key;
    private List<FieldDomain> fields;
    private List<Document> items;
}
