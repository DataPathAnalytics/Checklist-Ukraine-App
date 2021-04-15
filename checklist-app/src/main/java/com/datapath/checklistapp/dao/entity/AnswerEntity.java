package com.datapath.checklistapp.dao.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalDateTime;

@Data
@Node("Answer")
public class AnswerEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String values;
    private String comment;

    @CreatedDate
    private LocalDateTime dateCreated;
    @LastModifiedDate
    private LocalDateTime dateModified;

    @Relationship(type = "EXECUTED_BY")
    private QuestionExecutionEntity questionExecution;
}
