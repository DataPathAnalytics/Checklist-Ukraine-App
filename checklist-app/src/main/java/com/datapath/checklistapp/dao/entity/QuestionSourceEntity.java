package com.datapath.checklistapp.dao.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity(name = "question_source")
public class QuestionSourceEntity {

    @Id
    private Long identifier;
    private String name;
    private String link;
}
