package com.datapath.analyticapp.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.neo4j.core.schema.CompositeProperty;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@Node("AnswerStructure")
@NoArgsConstructor
@AllArgsConstructor
public class AnswerStructureEntity {

    @Id
    @GeneratedValue
    private Long id;
    private Long outerId;
    private String name;
    private String identifierFieldName;
    @CreatedDate
    private LocalDateTime dateExport;

    @CompositeProperty(prefix = "value")
    private Map<Long, String> values = new HashMap<>();
}
