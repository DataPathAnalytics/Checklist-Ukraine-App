package com.datapath.analyticapp.dao.service;

import com.datapath.analyticapp.dao.service.request.*;
import com.datapath.analyticapp.exception.CypherOperationException;
import lombok.AllArgsConstructor;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.nonNull;

@Service
@AllArgsConstructor
public class CypherQueryService {

    private static final String MERGE_IDENTIFIER_NODE_REQUEST =
            "merge (n:%s {%s : %s}) on create set n = $props on match set n += $props return id(n)";

    private static final String MERGE_NON_IDENTIFIER_NODE_REQUEST =
            "match (n) where id(n)=$parentId MERGE (n)-[:%s {answerId: $answerId}]->(a:%s) on create set a = $props on match set a += $props return id(a)";

    private static final String SIMPLE_RULE_RELATIONSHIP_REQUEST =
            "match (n), (sn) where id(n) = $nodeId and id(sn) in $secondNodeIds merge (n)%s[:%s]%s(sn)";

    private static final String BASED_ON_PARENT_RULE_RELATIONSHIP_REQUEST =
            "match (pn)-[:%s]->(sn), (n) where id(pn) in $parentNodeIds and id(n) = $nodeId merge (n)%s[:%s]%s(sn)";

    private static final String CREATE_RELATIONSHIP_REQUEST =
            "match (n1), (n2) where id(n1) = $parentId and id(n2) = $childId merge (n1)-[:%s]->(n2)";

    private static final String MERGE_FACT_QUESTION_REQUEST =
            "match (n) where id(n) = $parentId merge (n)-[:%s]->(f:%s {%s: $value, questionValue: $questionValue}) return id(f)";

    private static final String DELETE_REQUEST =
            "match (n)-[:%s]->(d:%s) where id(n) = $parentId detach delete d";

    private static final String EVENT_REQUEST =
            "match (n), (et:EventType) where id(n) = $parentId and id(et) = $eventTypeId create (n)-[:%s]->(e:%s {dateCreated: localdatetime()})-[:HAS_EVENT_TYPE]->(et) return id(e)";

    private final Neo4jClient client;

    public Long mergeNotIdentifierNode(NonIdentifierRequest request) {
        Map<String, Object> param = new HashMap<>();
        param.put("parentId", request.getParentId());
        param.put("props", request.getProps());
        param.put("answerId", request.getAnswerId());
        return client.query(
                String.format(MERGE_NON_IDENTIFIER_NODE_REQUEST,
                        request.getLinkType(),
                        request.getNodeType())
        ).bindAll(param).fetchAs(Long.class).one().orElseThrow(() -> new CypherOperationException(request.toString()));
    }

    public Long mergeIdentifierNode(IdentifierRequest request) {
        Map<String, Object> param = new HashMap<>();
        param.put("props", request.getProps());
        return client.query(
                String.format(MERGE_IDENTIFIER_NODE_REQUEST,
                        request.getNodeType(),
                        request.getIdentifierField(),
                        request.getIdentifierValue())
        ).bindAll(param).fetchAs(Long.class).one().orElseThrow(() -> new CypherOperationException(request.toString()));
    }

    public void buildRelationship(RelationshipRequest request) {
        Map<String, Object> param = new HashMap<>();
        param.put("parentId", request.getParentId());
        param.put("childId", request.getChildId());
        client.query(String.format(CREATE_RELATIONSHIP_REQUEST, request.getLinkType()))
                .bindAll(param).run();
    }

    public void buildRelationshipUseRule(RuleRelationshipRequest request) {
        Map<String, Object> param = new HashMap<>();
        param.put("nodeId", request.getNodeId());

        String query;
        if (nonNull(request.getRule().getSecondNodeType())) {
            param.put("secondNodeIds", request.getSecondNodeIds());
            query = String.format(SIMPLE_RULE_RELATIONSHIP_REQUEST,
                    request.getRule().getDirection().getStart(),
                    request.getRule().getLinkType(),
                    request.getRule().getDirection().getEnd());
        } else {
            param.put("parentNodeIds", request.getParentOfSecondNodeIds());
            query = String.format(BASED_ON_PARENT_RULE_RELATIONSHIP_REQUEST,
                    request.getParentOfSecondNodeLink(),
                    request.getRule().getDirection().getStart(),
                    request.getRule().getLinkType(),
                    request.getRule().getDirection().getEnd());
        }

        client.query(query).bindAll(param).run();
    }

    public Long mergeFactNode(FactRequest request) {
        Map<String, Object> param = new HashMap<>();
        param.put("parentId", request.getParentId());
        param.put("value", request.getValue());
        param.put("questionValue", request.getQuestionValue());

        return client.query(
                String.format(MERGE_FACT_QUESTION_REQUEST,
                        request.getLinkType(),
                        request.getNodeType(),
                        request.getValueName())
        ).bindAll(param).fetchAs(Long.class).one().orElseThrow(() -> new CypherOperationException(request.toString()));
    }

    public void delete(DeleteRequest request) {
        Map<String, Object> param = new HashMap<>();
        param.put("parentId", request.getParentId());
        client.query(String.format(DELETE_REQUEST, request.getLinkType(), request.getNodeType()))
                .bindAll(param).run();
    }

    public Long createEvent(EventRequest request) {
        Map<String, Object> param = new HashMap<>();
        param.put("parentId", request.getParentId());
        param.put("eventTypeId", request.getEventTypeId());
        return client.query(
                String.format(EVENT_REQUEST,
                        request.getLinkType(),
                        request.getNodeType())
        ).bindAll(param).fetchAs(Long.class).one().orElseThrow(() -> new CypherOperationException(request.toString()));
    }
}
