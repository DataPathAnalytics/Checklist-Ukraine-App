package com.datapath.checklistapp.dao.entity.classifier;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity(name = "permission")
public class Permission {

    @Id
    private Integer id;
    private String role;
    private String value;
}
