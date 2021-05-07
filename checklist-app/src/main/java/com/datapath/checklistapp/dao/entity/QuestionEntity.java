package com.datapath.checklistapp.dao.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity(name = "question")
public class QuestionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String value;
    @CreatedDate
    private LocalDateTime dateCreated;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "question_knowledge_class",
            joinColumns = {@JoinColumn(name = "question_id")},
            inverseJoinColumns = {@JoinColumn(name = "knowledge_class_id")},
            uniqueConstraints = @UniqueConstraint(columnNames = {"question_id", "knowledge_class_id"}))
    private Set<KnowledgeClassEntity> knowledgeClasses = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "answer_structure_id")
    private AnswerStructureEntity answerStructure;

    @ManyToOne
    @JoinColumn(name = "question_source_id")
    private QuestionSourceEntity questionSource;
}
