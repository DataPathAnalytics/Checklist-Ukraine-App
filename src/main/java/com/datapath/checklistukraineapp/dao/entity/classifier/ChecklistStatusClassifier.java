package com.datapath.checklistukraineapp.dao.entity.classifier;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Data
@Node("ChecklistStatus")
public class ChecklistStatusClassifier {
    @Id
    private Integer checklistStatusId;
    private String value;
}
