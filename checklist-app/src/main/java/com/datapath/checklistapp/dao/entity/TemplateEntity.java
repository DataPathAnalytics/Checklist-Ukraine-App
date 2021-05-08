package com.datapath.checklistapp.dao.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity(name = "template")
public class TemplateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @CreationTimestamp
    private LocalDateTime dateCreated;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private UserEntity author;

    @ManyToOne
    @JoinColumn(name = "folder_id")
    private TemplateFolderEntity folder;

    @ManyToOne
    @JoinColumn(name = "config_id")
    private TemplateConfigEntity config;

    @OneToMany
    @JoinColumn(name = "template_id")
    private Set<QuestionGroupEntity> groups = new HashSet<>();
}
