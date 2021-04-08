package com.datapath.checklistapp.dao.entity;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Data
@Node("AutoCompleteConfig")
public class AutoCompleteConfigEntity {
    @Id
    @GeneratedValue
    private Long id;
    private boolean autoComplete;
    private boolean searchable;
    private String datasource;
    private String fieldName;
    private String filterFieldName;

    @Relationship(type = "FOR_FIELD")
    private FieldDescriptionEntity field;
}
