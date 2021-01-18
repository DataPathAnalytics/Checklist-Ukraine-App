package com.datapath.checklistukraineapp.dao.entity;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

@Data
@Node("ViolationAmountCharacteristic")
public class ViolationAmountCharacteristicEntity {

    @Id
    @GeneratedValue
    private Long id;
    private Double amount;
    private Double paidAmount;
    private Double notPaidAmount;
    private Double eliminatedAmount;
    private Double notEliminatedAmount;
    private Double lossAmount;
    private Double notLossAmount;

    @Relationship(type = "HAS_DIRECTION_AMOUNT")
    private Set<FinancingDirectionAmountEntity> directionAmounts = new HashSet<>();
}
