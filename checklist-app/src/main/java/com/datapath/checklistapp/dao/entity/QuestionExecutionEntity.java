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
    private Integer id;

    private Integer parentQuestionId;
    private Integer conditionAnswerId;
    private String conditionFieldName;
    private Integer orderNumber;
    private Long linkTypeId;
    private Long nodeTypeId;
    private Long miningRoleId;
    private boolean required;
    private boolean root;
    @Enumerated(EnumType.STRING)
    private TemplateRole role;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private QuestionEntity question;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "question_execution_id")
    private Set<ConditionCharacteristicEntity> conditionCharacteristics = new HashSet<>();

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "question_execution_id")
    private Set<AutoCompleteConfigEntity> autoCompleteConfig = new HashSet<>();
}
