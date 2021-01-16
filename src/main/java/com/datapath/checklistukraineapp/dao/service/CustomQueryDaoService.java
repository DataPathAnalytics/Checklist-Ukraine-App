package com.datapath.checklistukraineapp.dao.service;

import lombok.AllArgsConstructor;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomQueryDaoService {

    private final static String CREATE_ONE_TO_ONE_RELATIONSHIP_QUERY_TEMPLATE =
            "match (n1:%s), (n2:%s) where id(n1) = %s and id(n2) = %s create (n1)-[:%s]->(n2)";

    private final static String DELETE_ONE_TO_ONE_RELATIONSHIP_QUERY_TEMPLATE =
            "match (n1:%s)-[r:%s]->(n2:%s) where id(n1) = %s and id(n2) = %s delete r";

    private final Neo4jClient client;

    public void createRelationship(String firstNodeLabel,
                                   Object firstNodeId,
                                   String secondNodeLabel,
                                   Object secondNodeId,
                                   String relationshipType) {
        client.query(String.format(
                CREATE_ONE_TO_ONE_RELATIONSHIP_QUERY_TEMPLATE,
                firstNodeLabel,
                secondNodeLabel,
                firstNodeId,
                secondNodeId,
                relationshipType)
        ).run();
    }

    public void deleteRelationship(String firstNodeLabel,
                                   Object firstNodeId,
                                   String secondNodeLabel,
                                   Object secondNodeId,
                                   String relationshipType) {
        client.query(String.format(
                DELETE_ONE_TO_ONE_RELATIONSHIP_QUERY_TEMPLATE,
                firstNodeLabel,
                relationshipType,
                secondNodeLabel,
                firstNodeId,
                secondNodeId)
        ).run();
    }
}
