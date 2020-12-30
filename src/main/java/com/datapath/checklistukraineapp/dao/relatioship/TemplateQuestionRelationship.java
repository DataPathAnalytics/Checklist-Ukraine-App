package com.datapath.checklistukraineapp.dao.relatioship;

import com.datapath.checklistukraineapp.dao.entity.QuestionEntity;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@Data
@RelationshipProperties
public class TemplateQuestionRelationship {

    @Id
    private String identifier;
    private String groupName;
    private Long parentQuestionId;
    private String conditionAnswer;

    @TargetNode
    private QuestionEntity question;
}
