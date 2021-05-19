package com.datapath.analyticapp.dao.service;

import com.datapath.analyticapp.dao.service.request.*;
import com.datapath.analyticapp.exception.CypherOperationException;
import com.datapath.analyticapp.service.miner.converter.ConvertTypeService;
import com.datapath.analyticapp.service.miner.converter.ValueType;
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
            "match (n) where id(n)=$parentId CREATE (n)-[r:%s {initiators: [$initiatorId]}]->(a:%s $props) return id(a)";

    private static final String NON_IDENTIFIER_NODE_EXISTS_REQUEST =
            "match (n)-[:%s]->(d:%s) where id(n)=$parentId return d";

    private static final String SIMPLE_RULE_RELATIONSHIP_REQUEST =
            "match (n), (sn) where id(n) = $nodeId and id(sn) in $secondNodeIds merge (n)%s[r:%s]%s(sn) " +
                    "on create set r.initiators = [$initiatorId] " +
                    "on match set r.initiators = coalesce(r.initiators, []) + $initiatorId";

    private static final String BASED_ON_PARENT_RULE_RELATIONSHIP_REQUEST =
            "match (pn)-[:%s]->(sn), (n) where id(pn) in $parentNodeIds and id(n) = $nodeId merge (n)%s[r:%s]%s(sn) " +
                    "on create set r.initiators = [$initiatorId] " +
                    "on match set r.initiators = coalesce(r.initiators, []) + $initiatorId";

    private static final String CREATE_RELATIONSHIP_REQUEST =
            "match (n1), (n2) where id(n1) = $parentId and id(n2) = $childId merge (n1)-[r:%s]->(n2) " +
                    "on create set r.initiators = [$initiatorId] " +
                    "on match set r.initiators = coalesce(r.initiators, []) + $initiatorId";

    private static final String CREATE_FACT_QUESTION_REQUEST =
            "match (n) where id(n) = $parentId create (n)-[r:%s {initiators: [$initiatorId]}]->(f:%s $props) return id(f)";

    private static final String FACT_QUESTION_EXISTS_REQUEST =
            "match (n)-[:%s]->(f:%s {questionValue: $questionValue, %s: $value}) where id(n) = $parentId return id(f)";

    private static final String EVENT_RELATIONSHIP_REQUEST =
            "match (n), (e:EventType) where id(n) = $factId and id(e) = $eventTypeId " +
                    "merge (n)-[r:%s]->(e) " +
                    "on create set r.initiators = [$initiatorId] " +
                    "on match set r.initiators = coalesce(r.initiators, []) + $initiatorId";

    private static final String ADD_INITIATOR_TO_RELATIONSHIP_REQUEST =
            "match (n)-[r]->(d) where id(n) = $parentId and id(d) = $childId set r.initiators = coalesce(r.initiators, []) + $initiatorId";

    private static final String GET_SOURCED_NODE_ID_REQUEST =
            "match (n)-[:SOURCED]->(d) where id(n) = $initiatorId return id(d)";

    private static final String NO_MORE_SOURCED_REQUEST =
            "match (n)<-[r:SOURCED]-(i) where id(n) = $nodeId and id(i) <> $initiatorId return count(r) = 0";

    private static final String DELETE_BY_ID_REQUEST =
            "match (n) where id(n) = $nodeId detach delete n";

    private static final String DELETE_INITIATOR_RELATIONSHIP_REQUEST =
            "match (n)-[r]-(d) where id(n) = $nodeId and size(r.initiators) = 1 and $initiatorId in r.initiators delete r";

    private static final String DELETE_INITIATOR_FROM_RELATIONSHIP_REQUEST =
            "match (n)-[r]-(d) where id(n) = $nodeId and size(r.initiators) > 1 and $initiatorId in r.initiators " +
                    "set r.initiators = [x IN r.initiators WHERE x <> $initiatorId];";

    private final Neo4jClient client;
    private final ConvertTypeService convertService;

    public Long mergeNotIdentifierNode(NonIdentifierRequest request) {
        Map<String, Object> param = new HashMap<>();
        param.put("parentId", request.getParentId());
        param.put("props", request.getProps());
        param.put("initiatorId", request.getInitiatorId());

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
                addToRelationshipCount(request.getParentId(), existedNode.get().asNode().id(), request.getInitiatorId());
                return existedNode.get().asNode().id();
            }
        }

        return creteNonIdentifierNode(request, param);
    }

    private void addToRelationshipCount(Long parentId, Long childId, Long initiatorId) {
        Map<String, Object> param = new HashMap<>();
        param.put("parentId", parentId);
        param.put("childId", childId);
        param.put("initiatorId", initiatorId);
        client.query(ADD_INITIATOR_TO_RELATIONSHIP_REQUEST).bindAll(param).run();
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
        param.put("initiatorId", request.getInitiatorId());
        client.query(String.format(CREATE_RELATIONSHIP_REQUEST, request.getLinkType()))
                .bindAll(param).run();
    }

    public void buildRelationshipUseRule(RuleRelationshipRequest request) {
        Map<String, Object> param = new HashMap<>();
        param.put("nodeId", request.getNodeId());
        param.put("initiatorId", request.getInitiatorId());

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
        param.put("initiatorId", request.getInitiatorId());

        Optional<Long> existedFact = client.query(
                String.format(FACT_QUESTION_EXISTS_REQUEST,
                        request.getLinkType(),
                        request.getNodeType(),
                        request.getFieldName())
        ).bindAll(param).fetchAs(Long.class).one();

        if (existedFact.isPresent()) {
            addToRelationshipCount(request.getParentId(), existedFact.get(), request.getInitiatorId());
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

    public void createEvent(EventRequest request) {
        Map<String, Object> param = new HashMap<>();
        param.put("factId", request.getFactId());
        param.put("eventTypeId", request.getEventTypeId());
        param.put("initiatorId", request.getInitiatorId());
        client.query(
                String.format(EVENT_RELATIONSHIP_REQUEST, request.getLinkType())
        ).bindAll(param).run();
    }

    public void deleteInitiatorData(Long initiatorId) {
        Map<String, Object> param = new HashMap<>();
        param.put("initiatorId", initiatorId);

        List<Long> sourcedIds = new ArrayList<>(client.query(GET_SOURCED_NODE_ID_REQUEST).bindAll(param).fetchAs(Long.class).all());

        sourcedIds.forEach(id -> processSourcedNode(id, initiatorId));

        param.put("nodeId", initiatorId);
        delete(param);
    }

    private void processSourcedNode(Long id, Long initiatorId) {
        Map<String, Object> param = new HashMap<>();
        param.put("nodeId", id);
        param.put("initiatorId", initiatorId);

        if (noMoreSourced(param)) {
            delete(param);
        } else {
            removeFromRelationshipCount(param);
        }
    }

    private void removeFromRelationshipCount(Map<String, Object> param) {
        client.query(DELETE_INITIATOR_RELATIONSHIP_REQUEST).bindAll(param).run();
        client.query(DELETE_INITIATOR_FROM_RELATIONSHIP_REQUEST).bindAll(param).run();
    }

    private boolean noMoreSourced(Map<String, Object> param) {
        return client.query(NO_MORE_SOURCED_REQUEST).bindAll(param).fetchAs(Boolean.class).one()
                .orElseThrow(() -> new CypherOperationException(CypherOperationException.NODE_NOT_PROCESSED, param.toString()));
    }

    private void delete(Map<String, Object> param) {
        client.query(DELETE_BY_ID_REQUEST).bindAll(param).run();
    }
}
