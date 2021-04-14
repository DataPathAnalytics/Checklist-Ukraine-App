package com.datapath.checklistapp.dao.entity;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Data
@Node("Answer")
public class AnswerEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String values;
    private String comment;

    @Relationship(type = "EXECUTED_BY")
    private QuestionExecutionEntity questionExecution;
}
