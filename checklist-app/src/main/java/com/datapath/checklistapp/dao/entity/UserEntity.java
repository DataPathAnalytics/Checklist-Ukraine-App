package com.datapath.checklistapp.dao.entity;

import com.datapath.checklistapp.dao.entity.classifier.Permission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Node("User")
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private boolean disable;
    private boolean locked;
    private boolean removed;
    private boolean superAdmin;
    @CreatedDate
    private LocalDateTime registeredDate;

    @Relationship(type = "HAS_PERMISSION", direction = Relationship.Direction.OUTGOING)
    private Permission permission;

    @Relationship(type = "HAS_EMPLOYMENT_PERIOD", direction = Relationship.Direction.OUTGOING)
    private List<EmploymentEntity> employments = new ArrayList<>();
}
