package com.datapath.checklistapp.dao.entity;

import com.datapath.checklistapp.dao.entity.classifier.TemplateType;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalDate;
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
    private LocalDate dateCreated;

    @Relationship(type = "IN_TYPE")
    private TemplateType type;

    @Relationship(type = "HAS_AUTHOR")
    private UserEntity author;

    @Relationship(type = "IN_FOLDER")
    private TemplateConfigFolderEntity folder;

    @Relationship(type = "HAS_QUESTION_EXECUTION")
    private Set<QuestionExecutionEntity> questionExecutions = new HashSet<>();
}
