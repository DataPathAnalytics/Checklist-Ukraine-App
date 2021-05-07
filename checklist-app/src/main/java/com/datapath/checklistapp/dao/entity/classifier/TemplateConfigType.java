package com.datapath.checklistapp.dao.entity.classifier;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity(name = "template_config_type")
public class TemplateConfigType {

    @Id
    private Integer id;
    private String value;
}
