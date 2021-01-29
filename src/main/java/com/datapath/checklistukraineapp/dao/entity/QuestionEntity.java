package com.datapath.checklistukraineapp.dao.entity;

import com.datapath.checklistukraineapp.dao.entity.classifier.KnowledgeCategory;
import com.datapath.checklistukraineapp.dao.entity.classifier.QuestionType;
import com.datapath.checklistukraineapp.dao.relatioship.QuestionSourceRelationship;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
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

    @Relationship(type = "IN_KNOWLEDGE_CATEGORY")
    private KnowledgeCategory knowledgeCategory;

    @Relationship(type = "HAS_ANSWER_STRUCTURE")
    private AnswerStructureEntity answerStructure;

    @Relationship(type = "IN_TYPE")
    private QuestionType type;

    @Relationship(type = "FROM_QUESTION_SOURCE")
    private QuestionSourceRelationship source;
}
