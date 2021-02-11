package com.datapath.checklistapp.dao.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.time.LocalDateTime;

@Data
@Node("TemplateFolder")
public class TemplateFolderEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @CreatedDate
    private LocalDateTime dateCreated;
}
