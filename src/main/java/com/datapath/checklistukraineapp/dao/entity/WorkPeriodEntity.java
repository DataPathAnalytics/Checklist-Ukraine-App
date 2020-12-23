package com.datapath.checklistukraineapp.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalDateTime;

@Data
@Node("WorkPeriod")
@AllArgsConstructor
@NoArgsConstructor
public class WorkPeriodEntity {

    @Id
    @GeneratedValue
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;

    @Relationship(type = "WORKED_FOR", direction = Relationship.Direction.OUTGOING)
    private DepartmentEntity department;

    public WorkPeriodEntity(LocalDateTime start, LocalDateTime end, DepartmentEntity department) {
        this.start = start;
        this.end = end;
        this.department = department;
    }
}
