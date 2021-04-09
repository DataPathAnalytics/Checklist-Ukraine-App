package com.datapath.checklistapp.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

@Data
@Node("AnswerStructure")
@NoArgsConstructor
@AllArgsConstructor
public class AnswerStructureEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private boolean _default;

    @Relationship(type = "HAS_FIELD_DESCRIPTION")
    private Set<FieldDescriptionEntity> fields = new HashSet<>();
}
