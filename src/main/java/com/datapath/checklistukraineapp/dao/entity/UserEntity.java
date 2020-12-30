package com.datapath.checklistukraineapp.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    private LocalDateTime registeredDateTime;

    @Relationship(type = "HAS_PERMISSION", direction = Relationship.Direction.OUTGOING)
    private PermissionEntity permission;

    @Relationship(type = "HAS_EMPLOYMENT_PERIOD", direction = Relationship.Direction.OUTGOING)
    private List<EmploymentEntity> employments = new ArrayList<>();
}
