package com.datapath.checklistukraineapp.dao.entity;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Data
@Node("QuestionExecution")
public class QuestionExecutionEntity {

    @Id
    @GeneratedValue
    private Long id;

    private Long parentQuestionId;
    private Integer conditionAnswerId;

    private Integer orderNumber;

    @Relationship(type = "OF_QUESTION")
    private QuestionEntity question;
}
