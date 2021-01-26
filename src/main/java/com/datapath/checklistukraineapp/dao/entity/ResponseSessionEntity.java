package com.datapath.checklistukraineapp.dao.entity;

import com.datapath.checklistukraineapp.dao.entity.classifier.SessionStatus;
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
@Node("ChecklistResponse")
public class ResponseSessionEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @CreatedDate
    private LocalDate dateCreated;
    @LastModifiedDate
    private LocalDate dateModified;
    private LocalDate checkingPeriodStart;
    private LocalDate checkingPeriodEnd;
    private Double amount;
    private Double spentAmount;
    private Double controlAmount;
    private Double violationAmount;
    private String summary;

    @Relationship(type = "TEMPLATED_BY")
    private TemplateEntity template;
    @Relationship(type = "HAS_AUTHOR")
    private UserEntity author;
    @Relationship(type = "HAS_REVIEWER")
    private UserEntity reviewer;
    @Relationship(type = "IN_STATUS")
    private SessionStatus status;
    @Relationship(type = "HAS_ANSWER")
    private Set<AnswerEntity> answers = new HashSet<>();
}
