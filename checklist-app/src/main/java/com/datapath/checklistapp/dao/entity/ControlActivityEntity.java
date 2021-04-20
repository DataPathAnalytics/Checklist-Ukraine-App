package com.datapath.checklistapp.dao.entity;

import com.datapath.checklistapp.dao.entity.classifier.ActivityStatus;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

@Data
@Node("ControlActivity")
public class ControlActivityEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Relationship(type = "IN_STATUS")
    private ActivityStatus status;

    @Relationship(type = "HAS_TEMPLATE")
    private Set<TemplateEntity> templates = new HashSet<>();

    @Relationship(type = "HAS_ACTIVITY_RESPONSE")
    private ResponseSessionEntity activityResponse;

    @Relationship(type = "HAS_SESSION_RESPONSE")
    private Set<ResponseSessionEntity> sessionResponses = new HashSet<>();
}
