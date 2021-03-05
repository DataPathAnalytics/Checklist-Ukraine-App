package com.datapath.checklistapp.dao.service;

import lombok.AllArgsConstructor;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;

@Service
@AllArgsConstructor
public class CustomQueryDaoService {

    private final static String CREATE_ONE_TO_ONE_RELATIONSHIP_QUERY_TEMPLATE =
            "match (n1:%s), (n2:%s) where %s = %s and %s = %s merge (n1)-[:%s]->(n2)";

    private final static String DELETE_ONE_TO_ONE_RELATIONSHIP_QUERY_TEMPLATE =
            "match (n1:%s)-[r:%s]->(n2:%s) where %s = %s and %s = %s delete r";

    private final Neo4jClient client;

    public void createRelationship(String firstNodeLabel,
                                   String firstNodeFieldName,
                                   Object firstNodeFieldValue,
                                   String secondNodeLabel,
                                   String secondNodeFieldName,
                                   Object secondNodeFieldValue,
                                   String relationshipType) {
        String firstIdentifier = isNull(firstNodeFieldName) ? "id(n1)" : "n1." + firstNodeFieldName;
        String secondIdentifier = isNull(secondNodeFieldName) ? "id(n2)" : "n2." + secondNodeFieldName;

        client.query(String.format(
                CREATE_ONE_TO_ONE_RELATIONSHIP_QUERY_TEMPLATE,
                firstNodeLabel,
                secondNodeLabel,
                firstIdentifier,
                firstNodeFieldValue,
                secondIdentifier,
                secondNodeFieldValue,
                relationshipType)
        ).run();
    }

    public void deleteRelationship(String firstNodeLabel,
                                   String firstNodeFieldName,
                                   Object firstNodeFieldValue,
                                   String secondNodeLabel,
                                   String secondNodeFieldName,
                                   Object secondNodeFieldValue,
                                   String relationshipType) {
        String firstIdentifier = isNull(firstNodeFieldName) ? "id(n1)" : "n1." + firstNodeFieldName;
        String secondIdentifier = isNull(secondNodeFieldName) ? "id(n2)" : "n2." + secondNodeFieldName;

        client.query(String.format(
                DELETE_ONE_TO_ONE_RELATIONSHIP_QUERY_TEMPLATE,
                firstNodeLabel,
                relationshipType,
                secondNodeLabel,
                firstIdentifier,
                firstNodeFieldValue,
                secondIdentifier,
                secondNodeFieldValue)
        ).run();
    }
}
