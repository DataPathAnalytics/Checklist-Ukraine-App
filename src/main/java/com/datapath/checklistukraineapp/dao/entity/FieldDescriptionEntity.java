package com.datapath.checklistukraineapp.dao.entity;

import com.datapath.checklistukraineapp.util.AnswerFieldType;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Data
@Node("FieldDescription")
public class FieldDescriptionEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;
    private AnswerFieldType type;
    private String dataSource;
}
