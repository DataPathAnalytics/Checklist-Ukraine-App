package com.datapath.checklistukraineapp.dao.entity;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.time.LocalDate;

@Data
@Node("Question")
public class QuestionEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private LocalDate dateCreated;
    private LocalDate dateModified;
}
