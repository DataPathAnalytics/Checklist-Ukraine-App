package com.datapath.checklistukraineapp.dao.relatioship;

import com.datapath.checklistukraineapp.dao.entity.DepartmentEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RelationshipProperties
@EqualsAndHashCode(of = {"start", "end"})
public class UserDepartment {

    //    @Id
//    @GeneratedValue
//    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;

    @TargetNode
    private DepartmentEntity department;

    public UserDepartment(DepartmentEntity department, LocalDateTime start, LocalDateTime end) {
        this.department = department;
        this.start = start;
        this.end = end;
    }
}
