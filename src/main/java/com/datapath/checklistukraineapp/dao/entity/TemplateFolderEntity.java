package com.datapath.checklistukraineapp.dao.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Node("TemplateFolder")
@EqualsAndHashCode(of = {"name"})
public class TemplateFolderEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private LocalDate dateCreated;
    private LocalDate dateModified;

    private boolean root = false;

    @Relationship(type = "HAS_CHILD_FOLDER")
    private Set<TemplateFolderEntity> children = new HashSet<>();
}
