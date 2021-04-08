package com.datapath.analyticapp.dao.entity;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

@Data
@Node("NodeType")
public class NodeTypeEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String nodeTypeName;

    @Relationship(type = "HAS_LINK_TYPE")
    private Set<LinkTypeEntity> linkTypes = new HashSet<>();
}
