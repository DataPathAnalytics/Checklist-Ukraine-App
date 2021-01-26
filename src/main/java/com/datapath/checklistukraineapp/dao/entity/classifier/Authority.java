package com.datapath.checklistukraineapp.dao.entity.classifier;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Data
@Node
public class Authority {
    @Id
    private Integer authorityId;
    private String value;
}
