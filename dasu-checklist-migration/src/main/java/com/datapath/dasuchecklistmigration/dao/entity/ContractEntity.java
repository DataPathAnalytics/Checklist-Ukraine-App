package com.datapath.dasuchecklistmigration.dao.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Entity(name = "contract")
public class ContractEntity {

    @Id
    private Integer id;
    private String contractId;
    private Double valueAmount;
    private String currency;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
