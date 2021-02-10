package com.datapath.checklistapp.dao.entity.classifier;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;

@Data
@Node
public class TemplateType {

    @Id
    private Integer templateTypeId;
    private String value;

    @Relationship("USE_QUESTION_TYPE")
    private List<QuestionType> questionTypes = new ArrayList<>();
}
