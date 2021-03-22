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
    private Integer conditionAnswerId;

    private Integer orderNumber;

    private boolean required;

    private String linkType;

    @Relationship(type = "OF_QUESTION")
    private QuestionEntity question;

    @Relationship(type = "HAS_CONDITION_CHARACTERISTIC")
    private Set<ConditionCharacteristicEntity> conditionCharacteristics = new HashSet<>();
}
