package com.datapath.checklistapp.dao.entity;

import com.datapath.checklistapp.util.converter.ComponentType;
import com.datapath.checklistapp.util.converter.ValueType;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

@Data
@Node("FieldDescription")
public class FieldDescriptionEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;
    private ValueType valueType;
    private ComponentType componentType;
    private String dataSource;
    private boolean title;
    private boolean required;
    private boolean identifier;

    @Relationship(type = "HAS_VALUE")
    private Set<ValueEntity> values = new HashSet<>();
}
