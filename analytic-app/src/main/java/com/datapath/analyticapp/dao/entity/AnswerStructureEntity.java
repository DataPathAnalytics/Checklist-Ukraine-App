package com.datapath.analyticapp.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.time.LocalDateTime;

@Data
@Node("AnswerStructure")
@NoArgsConstructor
@AllArgsConstructor
public class AnswerStructureEntity {

    @Id
    @GeneratedValue
    private Long id;
    private Long outerId;
    private String name;
    private String identifierFieldName;
    private String linkType;
    private String nodeType;
    @CreatedDate
    private LocalDateTime dateExport;
}
