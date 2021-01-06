package com.datapath.checklistukraineapp.dao.entity;

import lombok.Data;
import org.springframework.data.annotation.Version;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Data
@Node("ControlObject")
public class ControlObjectEntity {

    @Id
    private String controlObjectId;//edrpo
    private String name;
    @Version
    private Long version;
}
