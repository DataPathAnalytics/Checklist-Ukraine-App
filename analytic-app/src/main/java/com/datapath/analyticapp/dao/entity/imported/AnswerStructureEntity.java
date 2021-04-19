package com.datapath.analyticapp.dao.entity.imported;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Relationship(type = "HAS_FIELD_DESCRIPTION")
    private List<FieldDescriptionEntity> fields = new ArrayList<>();
}
