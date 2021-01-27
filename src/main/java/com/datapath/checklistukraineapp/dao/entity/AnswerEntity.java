package com.datapath.checklistukraineapp.dao.entity;

import com.datapath.checklistukraineapp.dao.entity.classifier.AnswerType;
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
    private Double violationAmount;
    private String comment;

    @Relationship(type = "TO_QUESTION")
    private QuestionEntity question;
    @Relationship(type ="IN_ANSWER")
    private AnswerType answer;

}