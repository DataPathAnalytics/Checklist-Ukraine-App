package com.datapath.checklistapp.dao.entity;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Data
@Node("QuestionSource")
public class QuestionSourceEntity {

    @Id
    private Long identifier;
    private String name;
    private String link;
}
