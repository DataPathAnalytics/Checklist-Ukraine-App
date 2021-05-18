package com.datapath.avtodormigration.dao.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity(name = "answer_type")
public class AnswerTypeEntity {

    @Id
    private Integer id;
}
