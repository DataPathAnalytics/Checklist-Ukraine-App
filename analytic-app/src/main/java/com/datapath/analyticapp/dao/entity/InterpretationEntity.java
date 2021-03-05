package com.datapath.analyticapp.dao.entity;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Data
@Node("Interpretation")
public class InterpretationEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String interpretation;
}
