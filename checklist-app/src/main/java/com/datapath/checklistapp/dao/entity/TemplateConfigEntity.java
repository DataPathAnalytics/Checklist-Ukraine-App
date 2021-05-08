package com.datapath.checklistapp.dao.entity;

import com.datapath.checklistapp.dao.entity.classifier.TemplateConfigType;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity(name = "template_config")
public class TemplateConfigEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @CreationTimestamp
    private LocalDateTime dateCreated;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private TemplateConfigType type;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private UserEntity author;

    @ManyToOne
    @JoinColumn(name = "folder_id")
    private TemplateConfigFolderEntity folder;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "template_config_id")
    private Set<QuestionExecutionEntity> questions = new HashSet<>();
}
