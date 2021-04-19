package com.datapath.analyticapp.dao.entity.imported;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalDateTime;

@Data
@Node("Employment")
public class EmploymentEntity {

    @Id
    @GeneratedValue
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;

    @Relationship(type = "EMPLOYMENT_BY")
    private DepartmentEntity department;
}
