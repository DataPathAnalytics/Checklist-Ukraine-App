package com.datapath.checklistapp.dao.entity.classifier;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Data
@Node
public class TemplateConfigType {

    @Id
    private Integer typeId;
    private String value;
}
