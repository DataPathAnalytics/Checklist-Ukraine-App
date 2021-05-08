package com.datapath.checklistapp.dao.entity;

import com.datapath.checklistapp.dao.entity.classifier.SessionStatus;
import com.datapath.checklistapp.util.database.SessionPlace;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity(name = "session")
public class SessionEntity {

    @Id
    @GeneratedValue
    private Integer id;
    private Integer number;
    private boolean autoCreated;
    private boolean invalid;
    @Enumerated(EnumType.STRING)
    private SessionPlace place;

    @CreationTimestamp
    private LocalDateTime dateCreated;
    @UpdateTimestamp
    private LocalDateTime dateModified;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id")
    private TemplateEntity template;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_config_id")
    private TemplateConfigEntity templateConfig;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private UserEntity author;

    @ManyToOne
    @JoinColumn(name = "reviewer_id")
    private UserEntity reviewer;

    @ManyToMany
    @JoinTable(name = "session_member",
            joinColumns = {@JoinColumn(name = "session_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")},
            uniqueConstraints = @UniqueConstraint(columnNames = {"session_id", "user_id"}))
    private Set<UserEntity> members = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "status_id")
    private SessionStatus status;

    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "session_id")
    private Set<AnswerEntity> answers = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "activity_id")
    private ControlActivityEntity activity;
}
