package com.datapath.checklistapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConditionCharacteristicDTO {
    private boolean evaluation;
    private Long outerRiskEventId;
    private Integer conditionAnswerId;
}