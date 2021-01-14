package com.datapath.checklistukraineapp.dao.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChecklistDomain {

    private Long id;
    private String status;
    private String name;
    private LocalDate dateCreated;
    private LocalDate dateModified;
    private LocalDate checkingPeriodStart;
    private LocalDate checkingPeriodEnd;
    private Double amount;
    private Double spentAmount;
    private Double controlAmount;
    private Double violationAmount;
    private String summary;
    private Long templateId;
    private String templateName;
    private Long reviewerId;
    private Long authorId;
}
