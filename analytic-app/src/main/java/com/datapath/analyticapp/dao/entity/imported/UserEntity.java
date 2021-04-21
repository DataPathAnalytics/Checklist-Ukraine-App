package com.datapath.analyticapp.dao.entity.imported;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.time.LocalDateTime;

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
    @CreatedDate
    private LocalDateTime exportDate;
}
