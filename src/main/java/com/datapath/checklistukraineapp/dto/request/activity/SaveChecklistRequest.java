package com.datapath.checklistukraineapp.dto.request.activity;

import com.datapath.checklistukraineapp.dto.ChecklistAnswerDTO;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
public class SaveChecklistRequest {

    private Long id;
    @NotBlank
    private String name;
    private LocalDate checkingPeriodStart;
    private LocalDate checkingPeriodEnd;
    private Double amount;
    private Double spentAmount;
    private Double controlAmount;
    private Double violationAmount;
    private String summary;
    @NotNull
    private Long eventId;
    @NotNull
    private Long templateId;

    private List<ChecklistAnswerDTO> answers;
}
