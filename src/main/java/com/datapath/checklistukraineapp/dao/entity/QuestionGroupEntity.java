package com.datapath.checklistukraineapp.dao.entity;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

@Data
@Node("QuestionGroup")
public class QuestionGroupEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private Integer orderNumber;

    @Relationship(type = "HAS_QUESTION_EXECUTION")
    private Set<QuestionExecutionEntity> questions = new HashSet<>();
}
