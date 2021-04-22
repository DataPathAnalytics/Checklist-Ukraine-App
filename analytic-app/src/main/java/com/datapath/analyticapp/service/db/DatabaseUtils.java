package com.datapath.analyticapp.service.db;

import lombok.AllArgsConstructor;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class DatabaseUtils {

    private static final String CREATE_ACTIVITY_AUTHOR_RELATIONSHIPS =
            "match (n:ControlActivity), (u:User) " +
                    "where id(n)=$controlActivityId and id(u)=$userId merge (n)-[:HAS_AUTHOR]->(u)";

    private final Neo4jClient client;

    public void updateAuthorRelationship(Long controlActivityId, Long userId) {
        Map<String, Object> param = new HashMap<>();
        param.put("controlActivityId", controlActivityId);
        param.put("userId", userId);

        client.query(CREATE_ACTIVITY_AUTHOR_RELATIONSHIPS).bindAll(param).run();
    }

    public void updateMemberRelationship(Long controlActivityId, List<Long> userIds) {

    }
}
