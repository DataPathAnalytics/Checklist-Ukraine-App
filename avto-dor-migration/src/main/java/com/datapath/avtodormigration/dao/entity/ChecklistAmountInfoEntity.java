package com.datapath.avtodormigration.dao.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity(name = "checklist_amount_info")
public class ChecklistAmountInfoEntity {

    @Id
    private Integer id;

    private Double totalContractAmountStateBudget;
    private Double totalContractAmountCovidFund;
    private Double totalContractAmountLocalBudget;
    private Double totalContractAmountOthers;

    private Double revisionPeriodTotalContractAmountStateBudget;
    private Double revisionPeriodTotalContractAmountCovidFund;
    private Double revisionPeriodTotalContractAmountLocalBudget;
    private Double revisionPeriodTotalContractAmountOthers;

    private Double budgetAmountStateBudget;
    private Double budgetAmountCovidFund;
    private Double budgetAmountLocalBudget;
    private Double budgetAmountOthers;

    private Double completedWorkAmountStateBudget;
    private Double completedWorkAmountCovidFund;
    private Double completedWorkAmountLocalBudget;
    private Double completedWorkAmountOthers;

    private Double checkedWorkAmountStateBudget;
    private Double checkedWorkAmountCovidFund;
    private Double checkedWorkAmountLocalBudget;
    private Double checkedWorkAmountOthers;
}
