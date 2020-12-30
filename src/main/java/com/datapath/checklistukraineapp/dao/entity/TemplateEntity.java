package com.datapath.checklistukraineapp.dao.entity;

import com.datapath.checklistukraineapp.dao.relatioship.TemplateQuestionRelationship;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Node("Template")
public class TemplateEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private LocalDate dateCreated;

    @Relationship(type = "HAS_AUTHOR")
    private UserEntity author;

    @Relationship(type = "IN_TEMPLATE_FOLDER")
    private TemplateFolderEntity folder;

    @Relationship(type = "HAS_QUESTION")
    private Set<TemplateQuestionRelationship> questions = new HashSet<>();
}
