package com.datapath.checklistapp.dao.entity;

import com.datapath.checklistapp.dao.entity.classifier.TemplateConfigType;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity(name = "template_config")
@EntityListeners(AuditingEntityListener.class)
public class TemplateConfigEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @CreatedDate
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
