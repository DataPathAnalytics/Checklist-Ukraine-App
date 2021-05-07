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
    private Long id;
    private LocalDateTime start;
    private LocalDateTime finish;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private DepartmentEntity department;

    public EmploymentEntity(LocalDateTime start, LocalDateTime finish, DepartmentEntity department) {
        this.start = start;
        this.finish = finish;
        this.department = department;
    }
}
