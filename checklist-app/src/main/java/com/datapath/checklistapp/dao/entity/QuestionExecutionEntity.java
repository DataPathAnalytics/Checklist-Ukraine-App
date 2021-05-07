package com.datapath.checklistapp.dao.entity;

import com.datapath.checklistapp.util.database.TemplateRole;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity(name = "question_execution")
public class QuestionExecutionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long parentQuestionId;
    private Long conditionAnswerId;
    private String conditionFieldName;
    private Integer orderNumber;
    private Long linkTypeId;
    private Long nodeTypeId;
    private Long roleId;
    private boolean required;
    private boolean root;

    private TemplateRole role;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private QuestionEntity question;

    @OneToMany
    @JoinColumn(name = "question_execution_id")
    private Set<ConditionCharacteristicEntity> conditionCharacteristics = new HashSet<>();

    @OneToMany
    @JoinColumn(name = "question_execution_id")
    private Set<AutoCompleteConfigEntity> autoCompleteConfig = new HashSet<>();
}
