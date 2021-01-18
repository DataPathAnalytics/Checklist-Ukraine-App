package com.datapath.checklistukraineapp.dao.entity;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

@Data
@Node("AmountCharacteristic")
public class AmountCharacteristicEntity {

    @Id
    @GeneratedValue
    private Long  id;
    private Double totalAmount;

    @Relationship(type = "HAS_TYPE_AMOUNT")
    private Set<FinancingTypeAmountEntity> typeAmounts = new HashSet<>();
    @Relationship(type = "HAS_DIRECTION_AMOUNT")
    private Set<FinancingDirectionAmountEntity> directionAmounts = new HashSet<>();
}
