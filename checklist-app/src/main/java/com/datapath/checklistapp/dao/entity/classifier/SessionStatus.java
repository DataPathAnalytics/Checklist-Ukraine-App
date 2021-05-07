package com.datapath.checklistapp.dao.entity.classifier;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity(name = "session_status")
public class SessionStatus {
    @Id
    private Integer id;
    private String value;
}
