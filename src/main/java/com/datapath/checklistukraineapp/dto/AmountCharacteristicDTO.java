package com.datapath.checklistukraineapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AmountCharacteristicDTO {

    private Double totalValue;
    private List<FinancingTypeAmountDTO> typeAmounts;
    private List<FinancingDirectionAmountDTO> directionAmounts;

}
