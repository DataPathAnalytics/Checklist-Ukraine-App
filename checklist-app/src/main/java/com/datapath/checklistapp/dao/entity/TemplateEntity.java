package com.datapath.checklistapp.dao.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity(name = "template")
public class TemplateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @CreatedDate
    private LocalDateTime dateCreated;

    @ManyToOne
    @JoinColumn(name = "user_id")
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
