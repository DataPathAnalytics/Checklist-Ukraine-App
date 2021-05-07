package com.datapath.checklistapp.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity(name = "department")
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentEntity {

    @Id
    private Long identifier;
    private String name;
}
