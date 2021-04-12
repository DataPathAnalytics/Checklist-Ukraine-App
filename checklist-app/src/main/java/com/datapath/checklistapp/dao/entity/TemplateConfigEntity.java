package com.datapath.checklistapp.dao.entity;

import com.datapath.checklistapp.dao.entity.classifier.TemplateConfigType;
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
@Node("TemplateConfig")
public class TemplateConfigEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @CreatedDate
    private LocalDateTime dateCreated;

    @Relationship(type = "IN_TYPE")
    private TemplateConfigType type;

    @Relationship(type = "HAS_AUTHOR")
    private UserEntity author;

    @Relationship(type = "IN_FOLDER")
    private TemplateConfigFolderEntity folder;

    @Relationship(type = "HAS_OBJECT_FUTURE_QUESTION")
    private Set<QuestionExecutionEntity> objectFutureQuestions = new HashSet<>();

    @Relationship(type = "HAS_TYPE_QUESTION")
    private Set<QuestionExecutionEntity> typeQuestions = new HashSet<>();

    @Relationship(type = "HAS_AUTHORITY_FEATURE_QUESTION")
    private Set<QuestionExecutionEntity> authorityFeatureQuestions = new HashSet<>();

    @Relationship(type = "HAS_OBJECT_QUESTION")
    private QuestionExecutionEntity objectQuestion;

    @Relationship(type = "HAS_AUTHORITY_QUESTION")
    private QuestionExecutionEntity authorityQuestion;
}
