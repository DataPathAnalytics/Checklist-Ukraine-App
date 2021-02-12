package com.datapath.checklistapp.dao.relatioship;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;

@Data
@RelationshipProperties
public class InterpretationRelationship {

    @Id
    @GeneratedValue
    private Long id;
    private Integer conditionAnswer;
}
