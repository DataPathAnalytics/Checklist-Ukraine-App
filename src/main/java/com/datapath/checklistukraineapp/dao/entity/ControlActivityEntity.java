package com.datapath.checklistukraineapp.dao.entity;

import com.datapath.checklistukraineapp.dao.entity.classifier.ActivityStatus;
import com.datapath.checklistukraineapp.dao.entity.classifier.Authority;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Node("ControlEvent")
public class ControlActivityEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @CreatedDate
    private LocalDate dateCreated;
    @LastModifiedDate
    private LocalDate dateModified;

    @Relationship(type = "IN_STATUS")
    private ActivityStatus status;
    @Relationship(type = "HAS_AUTHOR")
    private UserEntity author;
    @Relationship(type = "HAS_MEMBER")
    private Set<UserEntity> members = new HashSet<>();
    @Relationship(type = "IN_TYPE")
    private Authority type;
    @Relationship(type = "HAS_TEMPLATE")
    private Set<TemplateEntity> templates = new HashSet<>();
    @Relationship(type = "HAS_CHECKLIST")
    private Set<ResponseSessionEntity> checklists = new HashSet<>();
}
