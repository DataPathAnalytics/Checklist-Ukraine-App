package com.datapath.checklistapp.dao.entity;

import com.datapath.checklistapp.dao.entity.classifier.ActivityStatus;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity(name = "control_activity")
public class ControlActivityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private ActivityStatus status;

    @ManyToMany
    @JoinTable(name = "control_activity_template",
            joinColumns = {@JoinColumn(name = "control_activity_id")},
            inverseJoinColumns = {@JoinColumn(name = "template_id")},
            uniqueConstraints = @UniqueConstraint(columnNames = {"control_activity_id", "template_id"}))
    private Set<TemplateEntity> templates = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "activity_response_id")
    private ResponseSessionEntity activityResponse;

    @OneToMany(orphanRemoval = true, mappedBy = "activity")
    private Set<ResponseSessionEntity> sessionResponses = new HashSet<>();
}
