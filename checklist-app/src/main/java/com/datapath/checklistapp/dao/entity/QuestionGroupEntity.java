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
    private Integer id;
    private String name;
    private Integer orderNumber;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "group_id")
    private Set<QuestionExecutionEntity> questions = new HashSet<>();
}
