package com.datapath.checklistapp.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity(name = "employment")
@AllArgsConstructor
@NoArgsConstructor
public class EmploymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "department_id")
    private DepartmentEntity department;

    public EmploymentEntity(LocalDateTime startDate, LocalDateTime endDate, DepartmentEntity department) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.department = department;
    }
}
