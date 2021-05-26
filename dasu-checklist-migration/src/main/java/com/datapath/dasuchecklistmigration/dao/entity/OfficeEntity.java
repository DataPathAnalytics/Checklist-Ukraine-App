package com.datapath.dasuchecklistmigration.dao.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "dasu_office")
@Data
public class OfficeEntity {
    @Id
    private Integer id;
    private String name;
}
