package com.datapath.checklistapp.dao.entity;

import com.datapath.checklistapp.dao.entity.classifier.ActivityStatus;
import com.datapath.checklistapp.util.database.ResponseRole;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Data
@Entity(name = "control_activity")
public class ControlActivityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private ActivityStatus status;

    @ManyToMany
    @JoinTable(name = "control_activity_template",
            joinColumns = {@JoinColumn(name = "control_activity_id")},
            inverseJoinColumns = {@JoinColumn(name = "template_id")},
            uniqueConstraints = @UniqueConstraint(columnNames = {"control_activity_id", "template_id"}))
    private Set<TemplateEntity> templates = new HashSet<>();

    @OneToMany(orphanRemoval = true, mappedBy = "activity", cascade = CascadeType.ALL)
    private Set<SessionEntity> sessions = new HashSet<>();

    public SessionEntity getActivityResponse() {
        return this.getSessions().stream()
                .filter(s -> ResponseRole.ACTIVITY_RESPONSE.equals(s.getRole()))
                .findFirst()
                .orElse(null);
    }

    public Set<SessionEntity> getSessionResponses() {
        return this.getSessions().stream()
                .filter(s -> ResponseRole.SESSION_RESPONSE.equals(s.getRole()))
                .collect(toSet());
    }

    public void setActivityResponse(SessionEntity entity) {
        this.sessions.add(entity);
    }
}
