package com.datapath.checklistapp.dao.entity;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Data
@Node("FieldDefaultValue")
public class FieldDefaultValueEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String value;
}
