package com.datapath.checklistukraineapp.dao.entity.classifier;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Data
@Node("FinancingType")
public class FinancingTypeClassifier {

    @Id
    private Integer id;
    private String name;
}
