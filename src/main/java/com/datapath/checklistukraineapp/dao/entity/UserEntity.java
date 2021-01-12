package com.datapath.checklistukraineapp.dao.entity;

import com.datapath.checklistukraineapp.dao.entity.classifier.PermissionEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalDate;
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
    @CreatedDate
    private LocalDate registeredDate;

    @Relationship(type = "HAS_PERMISSION", direction = Relationship.Direction.OUTGOING)
    private PermissionEntity permission;

    @Relationship(type = "HAS_EMPLOYMENT_PERIOD", direction = Relationship.Direction.OUTGOING)
    private List<EmploymentEntity> employments = new ArrayList<>();
}
