package com.datapath.avtodormigration.dao.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "checklist")
public class ChecklistEntity {
    @Id
    private Integer id;
    private Integer templateId;
    private Integer statusId;

    private LocalDate startDate;

    private Integer tenderId;
    private Integer contractId;

    private LocalDate revisionPeriodStartDate;
    private LocalDate revisionPeriodEndDate;
    private LocalDate dfkPeriodStartDate;
    private LocalDate dfkPeriodEndDate;

    @ManyToOne
    @JoinColumn(name = "office_id")
    private OfficeEntity office;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "amount_info_id")
    private ChecklistAmountInfoEntity amountInfo;

    private String summary;

    @ManyToOne
    @JoinColumn(name = "auditor_id")
    private AuditorEntity auditor;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private ChecklistEventEntity event;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private BuyerEntity buyer;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "checklist_id")
    private List<AnswerEntity> answers = new ArrayList<>();
}
