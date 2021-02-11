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
@Node("Template")
public class TemplateEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @CreatedDate
    private LocalDateTime dateCreated;

    @Relationship(type = "HAS_AUTHOR")
    private UserEntity author;

    @Relationship(type = "IN_FOLDER")
    private TemplateFolderEntity folder;

    @Relationship(type = "IN_CONFIGURATION")
    private TemplateConfigEntity config;

    @Relationship(type = "HAS_QUESTION_GROUP")
    private Set<QuestionGroupEntity> groups = new HashSet<>();
}
