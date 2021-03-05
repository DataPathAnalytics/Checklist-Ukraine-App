package com.datapath.analyticapp.dao.entity;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Data
@Node("KnowledgeCategory")
public class KnowledgeCategoryEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String type;
    private String value;
    private String name;
    private String linkType;
}
