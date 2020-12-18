package com.datapath.checklistukraineapp.dao.relatioship;

import com.datapath.checklistukraineapp.dao.node.Department;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RelationshipProperties
public class UserToDepartment {

    @Id
    @GeneratedValue
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;

    @TargetNode
    private Department department;

    public UserToDepartment(Department department, LocalDateTime start, LocalDateTime end) {
        this.department = department;
        this.start = start;
        this.end = end;
    }
}
