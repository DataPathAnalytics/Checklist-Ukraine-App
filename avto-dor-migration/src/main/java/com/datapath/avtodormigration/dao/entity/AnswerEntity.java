package com.datapath.avtodormigration.dao.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@Entity(name = "question_answer")
public class AnswerEntity {

    @Id
    private Integer id;
    private String comment;

    @ManyToOne
    @JoinColumn(name = "answer_type_id")
    private AnswerTypeEntity answerType;

    private String categoryName;
    private String questionDescription;

    private Double amount;
    private Double violationAmount;
    private Double violationAmountCovidFund;
    private Double lossAmount;
    private Double lossAmountCovidFund;
    private Double eliminatedAmount;
    private Double eliminatedAmountCovidFund;
}
