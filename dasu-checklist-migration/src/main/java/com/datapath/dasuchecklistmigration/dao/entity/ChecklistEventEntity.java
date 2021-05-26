package com.datapath.dasuchecklistmigration.dao.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "checklist_event")
public class ChecklistEventEntity {

    @Id
    private Integer id;
    private String name;

}
