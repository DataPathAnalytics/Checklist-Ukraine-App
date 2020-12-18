package com.datapath.checklistukraineapp.dao.node;

import com.datapath.checklistukraineapp.dao.relatioship.UserToDepartment;
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
@Node
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String email;
    private String password;
    private String role;
    private boolean disable;
    private boolean locked;
    private boolean removed;
    private LocalDateTime registeredDateTime;

    @Relationship(type = "FROM", direction = Relationship.Direction.OUTGOING)
    private List<UserToDepartment> departments = new ArrayList<>();
}
