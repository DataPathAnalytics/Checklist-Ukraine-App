package com.datapath.checklistapp.dao.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Data
@Node("KnowledgeCategory")
@NoArgsConstructor
public class KnowledgeCategoryEntity {

    @Id
    @GeneratedValue
    private Long id;
    private Long outerId;

    public KnowledgeCategoryEntity(Long outerId) {
        this.outerId = outerId;
    }
}
