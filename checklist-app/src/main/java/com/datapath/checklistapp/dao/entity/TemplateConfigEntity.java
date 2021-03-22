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

    @Relationship(type = "HAS_FUTURE_QUESTION_EXECUTION")
    private Set<QuestionExecutionEntity> futureQuestionExecutions = new HashSet<>();

    @Relationship(type = "HAS_TYPE_QUESTION_EXECUTION")
    private Set<QuestionExecutionEntity> typeQuestionExecutions = new HashSet<>();

    @Relationship(type = "HAS_AUTHORITY_QUESTION_EXECUTION")
    private Set<QuestionExecutionEntity> authorityQuestionExecutions = new HashSet<>();

    @Relationship(type = "HAS_OBJECT_QUESTION_EXECUTION")
    private QuestionExecutionEntity objectQuestionExecution;
}
