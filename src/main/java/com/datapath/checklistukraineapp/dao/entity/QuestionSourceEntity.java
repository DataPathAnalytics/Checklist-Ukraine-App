package com.datapath.checklistukraineapp.dao.entity;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Data
@Node("QuestionSource")
public class QuestionSourceEntity {

    @Id
    private String id;
    private String name;
    private String link;
}
