package com.datapath.checklistapp.dao.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Data
@Node("ConditionCharacteristic")
@NoArgsConstructor
public class ConditionCharacteristicEntity {

    @Id
    @GeneratedValue
    private Long id;

    private boolean evaluation;
    private Long outerRiskEventTypeId;
    private Integer conditionAnswerId;

    public ConditionCharacteristicEntity(boolean evaluation, Long outerRiskEventTypeId, Integer conditionAnswerId) {
        this.conditionAnswerId = conditionAnswerId;
        this.evaluation = evaluation;
        this.outerRiskEventTypeId = outerRiskEventTypeId;
    }
}
