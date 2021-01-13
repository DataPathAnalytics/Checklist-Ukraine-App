package com.datapath.checklistukraineapp.dao.entity.classifier;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Data
@Node("Answer")
@AllArgsConstructor
@NoArgsConstructor
public class AnswerEntity {
    @Id
    private Integer answerId;
    private String value;
}
