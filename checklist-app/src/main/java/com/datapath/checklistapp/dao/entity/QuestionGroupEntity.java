package com.datapath.checklistapp.dao.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity(name = "question_group")
public class QuestionGroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer orderNumber;

    @OneToMany
    @JoinColumn(name = "group_id")
    private Set<QuestionExecutionEntity> questions = new HashSet<>();
}
