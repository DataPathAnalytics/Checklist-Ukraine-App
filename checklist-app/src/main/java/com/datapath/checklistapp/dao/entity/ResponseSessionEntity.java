package com.datapath.checklistapp.dao.entity;

import com.datapath.checklistapp.dao.entity.classifier.SessionStatus;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity(name = "response_session")
public class ResponseSessionEntity {

    @Id
    @GeneratedValue
    private Long id;
    private Integer number;
    private boolean autoCreated;
    private boolean invalid;

    @CreatedDate
    private LocalDateTime dateCreated;
    @LastModifiedDate
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
    @JoinTable(name = "response_session_member",
            joinColumns = {@JoinColumn(name = "response_session_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")},
            uniqueConstraints = @UniqueConstraint(columnNames = {"response_session_id", "user_id"}))
    private Set<UserEntity> members = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "status_id")
    private SessionStatus status;

    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "response_session_id")
    private Set<AnswerEntity> answers = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "activity_id")
    private ControlActivityEntity activity;
}
