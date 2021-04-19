package com.datapath.analyticapp.dao.entity.imported;

import lombok.Data;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Node("User")
public class UserEntity {

    @Id
    @GeneratedValue
    private Long id;
    private Long outerId;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDateTime registeredDate;
    private LocalDateTime dateModified;
    @LastModifiedDate
    private LocalDateTime exportDate;

    @Relationship(type = "HAS_EMPLOYMENT_PERIOD")
    private List<EmploymentEntity> employments = new ArrayList<>();
}
