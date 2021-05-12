package com.datapath.checklistapp.dao.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity(name = "question")
@EntityListeners(AuditingEntityListener.class)
public class QuestionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String value;
    @CreatedDate
    private LocalDateTime dateCreated;

    @ElementCollection
    @CollectionTable(name = "question_knowledge_class", joinColumns = @JoinColumn(name = "question_id"))
    @Column(name = "knowledge_class_outer_id", nullable = false)
    private Set<Long> knowledgeClasses = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "answer_structure_id")
    private AnswerStructureEntity answerStructure;

    @ManyToOne
    @JoinColumn(name = "question_source_id")
    private QuestionSourceEntity questionSource;
}
