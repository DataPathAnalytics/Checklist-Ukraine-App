package com.datapath.checklistukraineapp.dao.entity;

import com.datapath.checklistukraineapp.dao.entity.classifier.AnswerType;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashMap;
import java.util.Map;

@Data
@Node("Answer")
public class AnswerEntity {

    @Id
    @GeneratedValue
    private Long id;
    private Map<String, Object> values = new HashMap<>();
    private String comment;
    private Long parentFeatureId;

    @Relationship(type = "EXECUTED_BY")
    private QuestionExecutionEntity questionExecution;
    @Relationship(type ="IN_ANSWER_TYPE")
    private AnswerType answerType;

}
