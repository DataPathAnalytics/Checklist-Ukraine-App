package com.datapath.checklistapp.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Data
@Node("Department")
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String region;
}
