package com.datapath.checklistukraineapp.dao.entity;

import com.datapath.checklistukraineapp.dao.relatioship.QuestionSourceRelationship;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalDate;

@Data
@Node("Question")
public class QuestionEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @CreatedDate
    private LocalDate dateCreated;
    @LastModifiedDate
    private LocalDate dateModified;

    @Relationship(type = "FROM_QUESTION_SOURCE")
    private QuestionSourceRelationship source;
}
