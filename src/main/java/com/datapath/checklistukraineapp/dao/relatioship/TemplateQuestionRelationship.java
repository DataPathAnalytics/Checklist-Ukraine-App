package com.datapath.checklistukraineapp.dao.relatioship;

import com.datapath.checklistukraineapp.dao.entity.QuestionEntity;
import com.datapath.checklistukraineapp.util.Answer;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@Data
@RelationshipProperties
public class TemplateQuestionRelationship {

    @Id
    @GeneratedValue
    private Long id;
    private String groupName;
    private Long parentQuestionId;
    private Answer conditionAnswer;

    @TargetNode
    private QuestionEntity question;
}
