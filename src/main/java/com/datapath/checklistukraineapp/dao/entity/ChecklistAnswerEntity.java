package com.datapath.checklistukraineapp.dao.entity;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Data
@Node("ChecklistAnswer")
public class ChecklistAnswerEntity {

    @Id
    @GeneratedValue
    private Long id;
    private Double violationAmount;
    private String comment;

    @Relationship(type = "TO_QUESTION")
    private QuestionEntity question;
}
