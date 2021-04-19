package com.datapath.analyticapp.dao.entity.imported;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Data
@Node("Department")
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentEntity {

    @Id
    @GeneratedValue
    private Long id;
    private Long outerId;
    private String name;

    public DepartmentEntity(Long outerId, String name) {
        this.outerId = outerId;
        this.name = name;
    }
}
