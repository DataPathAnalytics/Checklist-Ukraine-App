package com.datapath.avtodormigration.dao.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity(name = "checklist")
public class ChecklistEntity {
    @Id
    private Integer id;
    @Column(name = "template_id")
    private Integer templateId;
    @Column(name = "status_id")
    private Integer statusId;

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
    private AuditorEntity auditor;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private ChecklistEventEntity event;

    @ManyToOne
    private BuyerEntity buyer;
}
