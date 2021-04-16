package com.datapath.analyticapp.dao.entity;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.CompositeProperty;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.HashMap;
import java.util.Map;

@Data
@Node("FieldDescription")
public class FieldDescriptionEntity {

    @Id
    @GeneratedValue
    private Long id;
    private Long outerId;
    private String name;
    private String type;

    @CompositeProperty(prefix = "value")
    private Map<String, String> values = new HashMap<>();
}
