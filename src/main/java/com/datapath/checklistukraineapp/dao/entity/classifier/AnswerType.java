package com.datapath.checklistukraineapp.dao.entity.classifier;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Data
@Node
public class AnswerType {
    @Id
    private Integer answerTypeId;
    private String value;
}
