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

    private String jsonValues;
    private String comment;
    private Long parentQuestionId;

    @Relationship(type = "EXECUTED_BY")
    private QuestionExecutionEntity questionExecution;
    @Relationship(type = "HAS_ANSWER")
    private ValueEntity value;

}
