package com.datapath.checklistukraineapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FinancingTypeAmountDTO {

    private Double amount;
    private Integer financingTypeId;
}
