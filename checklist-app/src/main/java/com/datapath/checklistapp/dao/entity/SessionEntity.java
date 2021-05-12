package com.datapath.checklistapp.dao.entity;

import com.datapath.checklistapp.dao.entity.classifier.SessionStatus;
import com.datapath.checklistapp.util.database.ResponseRole;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity(name = "session")
@EqualsAndHashCode(of = {"id"})
@EntityListeners(AuditingEntityListener.class)
public class SessionEntity {

    @Id
    @GeneratedValue
    private Integer id;
    private Integer number;
    private boolean autoCreated;
    private boolean invalid;
    @Enumerated(EnumType.STRING)
    private ResponseRole role;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private UserEntity author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id")
    private UserEntity reviewer;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "session_member",
            joinColumns = {@JoinColumn(name = "session_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")},
            uniqueConstraints = @UniqueConstraint(columnNames = {"session_id", "user_id"}))
    private Set<UserEntity> members = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "status_id")
    private SessionStatus status;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "session_id")
    private Set<AnswerEntity> answers = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id")
    private ControlActivityEntity activity;
}
