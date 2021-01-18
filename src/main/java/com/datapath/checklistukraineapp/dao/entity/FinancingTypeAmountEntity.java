package com.datapath.checklistukraineapp.dao.entity;

import com.datapath.checklistukraineapp.dao.entity.classifier.FinancingTypeClassifier;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Data
@Node("FinancingTypeAmount")
public class FinancingTypeAmountEntity {

    @Id
    @GeneratedValue
    private Long id;
    private Double amount;

    @Relationship(type = "IN_TYPE")
    private FinancingTypeClassifier type;
}
