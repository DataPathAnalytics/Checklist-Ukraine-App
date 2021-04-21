package com.datapath.analyticapp.dao.entity.imported;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.time.LocalDateTime;

@Data
@Node("ControlActivity")
public class ControlActivityEntity {
    @Id
    @GeneratedValue
    private Long id;
    private Long outerId;
    private LocalDateTime dateModified;
}
