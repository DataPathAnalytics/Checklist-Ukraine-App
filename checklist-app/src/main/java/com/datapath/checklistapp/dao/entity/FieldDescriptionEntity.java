package com.datapath.checklistapp.dao.entity;

import com.datapath.checklistapp.util.converter.AnswerFieldType;
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
    private AnswerFieldType type;
    private String dataSource;
    private String defaultValue;
    private boolean title;
    private boolean required;

    @Relationship(type = "HAS_DEFAULT_VALUE")
    private Set<FieldDefaultValueEntity> defaultValues = new HashSet<>();
}
