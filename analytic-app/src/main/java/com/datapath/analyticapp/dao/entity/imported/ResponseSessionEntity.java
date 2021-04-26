package com.datapath.analyticapp.dao.entity.imported;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalDateTime;

@Data
@Node("ResponseSession")
public class ResponseSessionEntity {
    @Id
    @GeneratedValue
    private Long id;
    private Long outerId;
    private LocalDateTime dateModified;
    private LocalDateTime dateCreated;
    @CreatedDate
    private LocalDateTime dateExported;
    @Relationship(type = "HAS_AUTHOR")
    private UserEntity author;
    @Relationship(type = "HAS_REVIEWER")
    private UserEntity reviewer;
}
