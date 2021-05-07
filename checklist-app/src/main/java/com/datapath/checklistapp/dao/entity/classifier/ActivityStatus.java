package com.datapath.checklistapp.dao.entity.classifier;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity(name = "activity_status")
public class ActivityStatus {
    @Id
    private Integer id;
    private String value;
}
