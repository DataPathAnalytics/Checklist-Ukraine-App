package com.datapath.checklistapp.dao.entity;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

@Data
@Node("QuestionExecution")
public class QuestionExecutionEntity {

    @Id
    @GeneratedValue
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

    @Relationship(type = "OF_QUESTION")
    private QuestionEntity question;

    @Relationship(type = "HAS_CONDITION_CHARACTERISTIC")
    private Set<ConditionCharacteristicEntity> conditionCharacteristics = new HashSet<>();

    @Relationship(type = "HAS_AUTOCOMPLETE_CONFIG")
    private Set<AutoCompleteConfigEntity> autoCompleteConfig = new HashSet<>();
}
