package com.datapath.avtodormigration.dao.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "auditor")
@Data
public class AuditorEntity {

    @Id
    private Integer id;
    private String email;
    private String name;
}
