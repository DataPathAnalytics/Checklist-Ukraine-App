package com.datapath.checklistukraineapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FinancingDirectionAmountDTO {

    private Double amount;
    private Integer financingDirectionId;
}
