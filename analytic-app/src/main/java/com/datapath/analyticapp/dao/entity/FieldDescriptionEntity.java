package com.datapath.analyticapp.dao.entity;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Data
@Node("FieldDescription")
public class FieldDescriptionEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;
//    private ValueType valueType;
//    private ComponentType componentType;
    private String dataSource;
    private boolean title;
    private boolean required;
    private boolean identifier;

//    @Relationship(type = "HAS_DEFAULT_VALUE")
//    private Set<DefaultValueEntity> defaultValues = new HashSet<>();
}
