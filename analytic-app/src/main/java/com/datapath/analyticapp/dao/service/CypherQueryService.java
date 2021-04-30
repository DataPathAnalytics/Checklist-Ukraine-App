package com.datapath.analyticapp.dao.service;

import com.datapath.analyticapp.dao.service.request.*;
import com.datapath.analyticapp.dto.imported.response.ValueType;
import com.datapath.analyticapp.exception.CypherOperationException;
import com.datapath.analyticapp.service.miner.converter.ConvertTypeService;
import lombok.AllArgsConstructor;
import org.neo4j.driver.internal.value.NodeValue;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@AllArgsConstructor
public class CypherQueryService {

    private static final String MERGE_IDENTIFIER_NODE_REQUEST =
            "merge (n:%s {%s : $identifierValue}) on create set n = $props on match set n += $props return id(n)";

    private static final String CREATE_NON_IDENTIFIER_NODE_REQUEST =
            "match (n) where id(n)=$parentId CREATE (n)-[r:%s {sourcedCount: 1}]->(a:%s $props) return id(a)";

    private static final String NON_IDENTIFIER_NODE_EXISTS_REQUEST =
            "match (n)-[:%s]->(d:%s) where id(n)=$parentId return d";

    private static final String SIMPLE_RULE_RELATIONSHIP_REQUEST =
            "match (n), (sn) where id(n) = $nodeId and id(sn) in $secondNodeIds merge (n)%s[:%s]%s(sn)";

    private static final String BASED_ON_PARENT_RULE_RELATIONSHIP_REQUEST =
            "match (pn)-[:%s]->(sn), (n) where id(pn) in $parentNodeIds and id(n) = $nodeId merge (n)%s[r:%s]%s(sn) " +
                    "on create set r.sourcedCount = 1 " +
                    "on match set r.sourcedCount = r.sourcedCount + 1";

    private static final String CREATE_RELATIONSHIP_REQUEST =
            "match (n1), (n2) where id(n1) = $parentId and id(n2) = $childId merge (n1)-[r:%s]->(n2) " +
                    "on create set r.sourcedCount = 1 " +
                    "on match set r.sourcedCount = r.sourcedCount + 1";

    private static final String CREATE_FACT_QUESTION_REQUEST =
            "match (n) where id(n) = $parentId create (n)-[r:%s {sourcedCount: 1}]->(f:%s $props) return id(f)";

    private static final String FACT_QUESTION_EXISTS_REQUEST =
            "match (n)-[:%s]->(f:%s {questionValue: $questionValue, %s: $value}) where id(n) = $parentId return id(f)";

    private static final String DELETE_REQUEST =
            "match (n)-[:%s]->(d:%s) where id(n) = $parentId detach delete d";

    private static final String EVENT_REQUEST =
            "match (n), (et:EventType) where id(n) = $parentId and id(et) = $eventTypeId " +
                    "merge (n)-[r:%s]->(e:%s)-[:HAS_EVENT_TYPE]->(et) " +
                    "on create set r.sourcedCount = 1, e.dateCreated = localdatetime() " +
                    "on match set r.sourcedCount = r.sourcedCount + 1, e.dateModified = localdatetime() return id(e)";

    private static final String UPDATE_SOURCED_COUNT_REQUEST =
            "match (n)-[r]->(d) where id(n) = $parentId and id(d) = $childId set r.sourcedCount = r.sourcedCount + 1";

    private final Neo4jClient client;
    private final ConvertTypeService convertService;

    public Long mergeNotIdentifierNode(NonIdentifierRequest request) {
        Map<String, Object> param = new HashMap<>();
        param.put("parentId", request.getParentId());
        param.put("props", request.getProps());

        List<NodeValue> nodes = new ArrayList<>(client.query(
                String.format(NON_IDENTIFIER_NODE_EXISTS_REQUEST,
                        request.getLinkType(),
                        request.getNodeType())
        ).bindAll(param).fetchAs(NodeValue.class).all());

        if (!isEmpty(nodes)) {
            Optional<NodeValue> existedNode = nodes.stream()
                    .filter(n -> hasIdentityProperties(n.asMap(), request.getProps(), request.getFieldTypes()))
                    .findFirst();

            if (existedNode.isPresent()) {
                updateRelationshipSourceCount(request.getParentId(), existedNode.get().asNode().id());
                return existedNode.get().asNode().id();
            }
        }

        return creteNonIdentifierNode(request, param);
    }

    private void updateRelationshipSourceCount(Long parentId, Long childId) {
        Map<String, Object> param = new HashMap<>();
        param.put("parentId", parentId);
        param.put("childId", childId);
        client.query(UPDATE_SOURCED_COUNT_REQUEST).bindAll(param).run();
    }

    private boolean hasIdentityProperties(Map<String, Object> existedProperty, Map<String, Object> newProperty, Map<String, ValueType> fieldTypes) {
        if (newProperty.size() != existedProperty.size()) return false;

        for (Map.Entry<String, Object> newProp : newProperty.entrySet()) {
            if (isNull(newProp.getValue())) continue;

            ValueType type = fieldTypes.get(newProp.getKey());

            Object existedValue = convertService.convert(existedProperty.get(newProp.getKey()), type);

            if (!newProp.getValue().equals(existedValue)) {
                return false;
            }
        }

        return true;
    }

    private Long creteNonIdentifierNode(NonIdentifierRequest request, Map<String, Object> param) {
        return client.query(
                String.format(CREATE_NON_IDENTIFIER_NODE_REQUEST,
                        request.getLinkType(),
                        request.getNodeType())
        ).bindAll(param).fetchAs(Long.class).one().orElseThrow(() -> new CypherOperationException(request.toString()));
    }

    public Long mergeIdentifierNode(IdentifierRequest request) {
        Map<String, Object> param = new HashMap<>();
        param.put("props", request.getProps());
        param.put("identifierValue", request.getIdentifierValue());
        return client.query(
                String.format(MERGE_IDENTIFIER_NODE_REQUEST,
                        request.getNodeType(),
                        request.getIdentifierField())
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
        param.put("questionValue", request.getQuestionValue());
        param.put("value", request.getProps().get(request.getFieldName()));
        param.put("props", request.getProps());

        Optional<Long> existedFact = client.query(
                String.format(FACT_QUESTION_EXISTS_REQUEST,
                        request.getLinkType(),
                        request.getNodeType(),
                        request.getFieldName())
        ).bindAll(param).fetchAs(Long.class).one();

        if (existedFact.isPresent()) {
            updateRelationshipSourceCount(request.getParentId(), existedFact.get());
            return existedFact.get();
        }

        request.getProps().put("questionValue", request.getQuestionValue());

        return client.query(String.format(CREATE_FACT_QUESTION_REQUEST,
                request.getLinkType(),
                request.getNodeType())
        ).bindAll(param)
                .fetchAs(Long.class)
                .one()
                .orElseThrow(() -> new CypherOperationException(request.toString()));
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
