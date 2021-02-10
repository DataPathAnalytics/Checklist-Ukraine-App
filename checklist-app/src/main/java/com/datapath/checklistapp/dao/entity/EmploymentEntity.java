package com.datapath.checklistapp.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalDate;

@Data
@Node("Employment")
@AllArgsConstructor
@NoArgsConstructor
public class EmploymentEntity {

    @Id
    @GeneratedValue
    private Long id;
    private LocalDate start;
    private LocalDate end;

    @Relationship(type = "EMPLOYMENT_BY", direction = Relationship.Direction.OUTGOING)
    private DepartmentEntity department;

    public EmploymentEntity(LocalDate start, LocalDate end, DepartmentEntity department) {
        this.start = start;
        this.end = end;
        this.department = department;
    }
}
