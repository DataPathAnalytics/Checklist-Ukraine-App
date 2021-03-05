package com.datapath.analyticapp.dao.entity;

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
    private String name;
    private LocalDateTime dateCreated;
    @CreatedDate
    private LocalDateTime dateExport;
    private Integer outerTypeId;

    @Relationship(type = "IN_KNOWLEDGE_CATEGORY")
    private Set<KnowledgeCategoryEntity> knowledgeCategory = new HashSet<>();

    @Relationship(type = "HAS_ANSWER_STRUCTURE")
    private AnswerStructureEntity answerStructure;
}
