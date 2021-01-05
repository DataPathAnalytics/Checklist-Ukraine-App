package com.datapath.checklistukraineapp.dao.entity.classifier;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Data
@Node("ControlType")
public class ControlTypeEntity {
    @Id
    private Integer controlTypeId;
    private String value;
}
