package com.datapath.checklistukraineapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChecklistAnswerDTO {

    @NotNull
    private Long questionId;
    private Integer answerId;

    private Double violationAmount;
    private String comment;

    private AmountCharacteristicDTO amountCharacteristic;
    private ViolationAmountCharacteristicDTO violationAmountCharacteristic;
}
