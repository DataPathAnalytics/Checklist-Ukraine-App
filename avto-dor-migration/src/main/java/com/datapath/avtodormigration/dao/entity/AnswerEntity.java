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
    private String npa;
    private Double amount;

    @ManyToOne
    @JoinColumn(name = "answer_type_id")
    private AnswerTypeEntity answerType;

    private String categoryName;
    private String categoryNumber;
    private String questionDescription;
    private Boolean baseQuestion;
    private String questionNumber;

    private Double violationAmount;
    private Double violationAmountCovidFund;
    private Double lossAmount;
    private Double lossAmountCovidFund;
    private Double eliminatedAmount;
    private Double eliminatedAmountCovidFund;
}
