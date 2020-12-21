package com.datapath.checklistukraineapp.dao.entity;

import com.datapath.checklistukraineapp.dao.relatioship.UserDepartment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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
    private String role;
    private boolean disable;
    private boolean locked;
    private boolean removed;
    private LocalDateTime registeredDateTime;

    @Relationship(type = "FROM", direction = Relationship.Direction.OUTGOING)
    private Set<UserDepartment> departments = new HashSet<>();
}
