package com.datapath.checklistapp.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node("LinkType")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinkTypeEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String linkType;

    public LinkTypeEntity(String linkType) {
        this.linkType = linkType;
    }
}
