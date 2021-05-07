package com.datapath.checklistapp.dao.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity(name = "condition_characteristic")
@NoArgsConstructor
public class ConditionCharacteristicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long outerRiskEventTypeId;
    private Long conditionAnswerId;

    public ConditionCharacteristicEntity(Long outerRiskEventTypeId, Long conditionAnswerId) {
        this.conditionAnswerId = conditionAnswerId;
        this.outerRiskEventTypeId = outerRiskEventTypeId;
    }
}
