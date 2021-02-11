package com.datapath.checklistapp.dao.entity;

import com.datapath.checklistapp.dao.entity.classifier.QuestionType;
import com.datapath.checklistapp.dao.relatioship.QuestionSourceRelationship;
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
    @CreatedDate
    private LocalDateTime dateCreated;

    @Relationship(type = "IN_KNOWLEDGE_CATEGORY")
    private Set<KnowledgeCategoryEntity> knowledgeCategory = new HashSet<>();

    @Relationship(type = "HAS_ANSWER_STRUCTURE")
    private AnswerStructureEntity answerStructure;

    @Relationship(type = "IN_TYPE")
    private QuestionType type;

    @Relationship(type = "FROM_QUESTION_SOURCE")
    private QuestionSourceRelationship source;
}
