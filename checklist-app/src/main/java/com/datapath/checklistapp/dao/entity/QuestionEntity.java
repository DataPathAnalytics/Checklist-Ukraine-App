package com.datapath.checklistapp.dao.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Node("Question")
public class QuestionEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String value;
    @CreatedDate
    private LocalDateTime dateCreated;

    @Relationship(type = "IN_KNOWLEDGE_CLASS")
    private Set<KnowledgeClassEntity> knowledgeClasses = new HashSet<>();

    @Relationship(type = "HAS_ANSWER_STRUCTURE")
    private AnswerStructureEntity answerStructure;

    @Relationship(type = "FROM_SOURCE")
    private QuestionSourceEntity questionSource;
}
