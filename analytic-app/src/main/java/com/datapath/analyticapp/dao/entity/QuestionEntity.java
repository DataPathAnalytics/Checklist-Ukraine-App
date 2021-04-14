package com.datapath.analyticapp.dao.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Node("Question")
public class QuestionEntity {

    @Id
    @GeneratedValue
    private Long id;
    private Long outerId;
    private String value;
    private LocalDateTime dateCreated;
    @CreatedDate
    private LocalDateTime dateExport;

    @Relationship(type = "IN_KNOWLEDGE_CLASS")
    private List<KnowledgeClassEntity> knowledgeClasses = new ArrayList<>();

    @Relationship(type = "HAS_ANSWER_STRUCTURE")
    private AnswerStructureEntity answerStructure;
}
