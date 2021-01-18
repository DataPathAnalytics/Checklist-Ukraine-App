package com.datapath.checklistukraineapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViolationAmountCharacteristicDTO {

    private Double amount;
    private Double paidAmount;
    private Double notPaidAmount;
    private Double eliminatedAmount;
    private Double notEliminatedAmount;
    private Double lossAmount;
    private Double notLossAmount;

    private List<FinancingDirectionAmountDTO> directionAmounts;
}
