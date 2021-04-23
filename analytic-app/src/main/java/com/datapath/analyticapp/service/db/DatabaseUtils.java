package com.datapath.analyticapp.service.db;

import com.datapath.analyticapp.service.miner.MinerRule;
import lombok.AllArgsConstructor;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class DatabaseUtils {

    private static final String MERGE_IDENTIFIER_NODE_REQUEST =
            "merge (n:%s {%s : %s}) on create set n = $props on match set n += $props return id(n)";

    private static final String UPDATE_NON_IDENTIFIER_NODE =
            "match (n) where id(n)=$parentId MERGE (n)-[:%s]->(a:%s) on create set a = $props on match set a += $props return id(a)";

    private static final String UPDATE_IDENTIFIER_NODE =
            "match (n) where id(n)=$parentId MERGE (n)-[:%s]->(a:%s {%s : %s}) on create set a = $props on match set a += $props return id(a)";

    private static final String SIMPLE_RULE_RELATIONSHIP = "match (n), (sn) where id(n) = $nodeId and id(sn) = $secondNodeId merge (n)%s[:%s]%s(sn)";

    private static final String BASED_ON_PARENT_RULE_RELATIONSHIP = "match (pn)-[:%s]->(sn), (n) where id(pn) = $parentNodeId and id(n) = $nodeId merge (n)%s[:%s]%s(sn)";

    private final Neo4jClient client;

    public Long updateNotIdentifierNode(QueryRequest request) {
        Map<String, Object> param = new HashMap<>();
        param.put("parentId", request.getId());
        param.put("props", request.getParams());
        return client.query(
                String.format(UPDATE_NON_IDENTIFIER_NODE,
                        request.getRelType(),
                        request.getAddNodeType())
        ).bindAll(param).fetchAs(Long.class).one().get();
    }

    public Long updateIdentifierNode(QueryRequest request) {
        Map<String, Object> param = new HashMap<>();
        param.put("parentId", request.getId());
        param.put("props", request.getParams());
        return client.query(
                String.format(UPDATE_IDENTIFIER_NODE,
                        request.getRelType(),
                        request.getAddNodeType(),
                        request.getIdentifierField(),
                        request.getIdentifierValue())
        ).bindAll(param).fetchAs(Long.class).one().get();
    }

    public Long mergeIdentifierNode(QueryRequest request) {
        Map<String, Object> param = new HashMap<>();
        param.put("props", request.getParams());
        return client.query(
                String.format(MERGE_IDENTIFIER_NODE_REQUEST,
                        request.getNodeType(),
                        request.getIdentifierField(),
                        request.getIdentifierValue())
        ).bindAll(param).fetchAs(Long.class).one().get();
    }

    public void buildRelationshipUseRule(MinerRule rule, Long nodeId, Long secondNodeId) {
        Map<String, Object> param = new HashMap<>();
        param.put("nodeId", nodeId);
        param.put("secondNodeId", secondNodeId);
        client.query(
                String.format(SIMPLE_RULE_RELATIONSHIP,
                        rule.getDirection().getStart(),
                        rule.getLinkType(),
                        rule.getDirection().getEnd())
        ).bindAll(param).run();
    }

    public void buildRelationshipUseRule(MinerRule rule, Long nodeId, Long parentOfSecondNodeId, String parentOfSecondNodeLink) {
        Map<String, Object> param = new HashMap<>();
        param.put("nodeId", nodeId);
        param.put("parentNodeId", parentOfSecondNodeId);
        client.query(
                String.format(BASED_ON_PARENT_RULE_RELATIONSHIP,
                        parentOfSecondNodeLink,
                        rule.getDirection().getStart(),
                        rule.getLinkType(),
                        rule.getDirection().getEnd())
        ).bindAll(param).run();
    }
}
