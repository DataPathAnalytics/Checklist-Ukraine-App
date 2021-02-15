package com.datapath.checklistapp.dao.relatioship;

import com.datapath.checklistapp.dao.entity.InterpretationEntity;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@Data
@RelationshipProperties
public class InterpretationRelationship {

    @Id
    @GeneratedValue
    private Long id;
    private Integer conditionAnswer;

    @TargetNode
    private InterpretationEntity interpretation;
}
