package com.datapath.analyticapp.dao.service;

import com.datapath.analyticapp.dao.service.request.*;
import com.datapath.analyticapp.dto.imported.response.ValueType;
import com.datapath.analyticapp.service.miner.converter.ConvertTypeService;
import com.datapath.analyticapp.service.miner.rule.MinerRule;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.datapath.analyticapp.dao.Node.FACT_DEFAULT_NODE;
import static com.datapath.analyticapp.dao.Relationship.EVENT_DEFAULT_LINK;
import static com.datapath.analyticapp.dao.Relationship.FACT_DEFAULT_LINK;

@Data
@Service
@AllArgsConstructor
public class QueryRequestBuilder {

    private final ConvertTypeService convertService;

    public IdentifierRequest identifierRequest(String nodeType,
                                               String identifierField,
                                               Object identifierValue,
                                               Map<String, Object> props,
                                               Map<String, ValueType> fieldTypes) {
        return IdentifierRequest.builder()
                .nodeType(nodeType)
                .identifierField(identifierField)
                .identifierValue(identifierValue)
                .props(convertProps(props, fieldTypes))
                .build();
    }

    public NonIdentifierRequest nonIdentifierRequest(Long parentId,
                                                     String nodeType,
                                                     String linkType,
                                                     Map<String, Object> props,
                                                     Map<String, ValueType> fieldTypes,
                                                     Long initiatorId) {
        return NonIdentifierRequest.builder()
                .nodeType(nodeType)
                .parentId(parentId)
                .linkType(linkType)
                .props(convertProps(props, fieldTypes))
                .fieldTypes(fieldTypes)
                .initiatorId(initiatorId)
                .build();
    }

    public RelationshipRequest relationshipRequest(Long parentId,
                                                   Long childId,
                                                   String linkType,
                                                   Long initiatorId) {
        return RelationshipRequest.builder()
                .parentId(parentId)
                .childId(childId)
                .linkType(linkType)
                .initiatorId(initiatorId)
                .build();
    }

    public RuleRelationshipRequest ruleRelationshipRequest(MinerRule rule,
                                                           Long nodeId,
                                                           List<Long> secondNodeIds,
                                                           List<Long> parentOfSecondNodeIds,
                                                           String parentOfSecondNodeLink,
                                                           Long initiatorId) {
        return RuleRelationshipRequest.builder()
                .rule(rule)
                .nodeId(nodeId)
                .secondNodeIds(secondNodeIds)
                .parentOfSecondNodeIds(parentOfSecondNodeIds)
                .parentOfSecondNodeLink(parentOfSecondNodeLink)
                .initiatorId(initiatorId)
                .build();
    }

    public FactRequest factRequest(Long parentId,
                                   String fieldName,
                                   String questionValue,
                                   Map<String, Object> props,
                                   Map<String, ValueType> fieldTypes,
                                   Long initiatorId) {
        return FactRequest.builder()
                .parentId(parentId)
                .fieldName(fieldName)
                .questionValue(questionValue)
                .props(convertProps(props, fieldTypes))
                .nodeType(FACT_DEFAULT_NODE)
                .linkType(FACT_DEFAULT_LINK)
                .initiatorId(initiatorId)
                .build();
    }

    public EventRequest eventRequest(Long factId, Long eventTypeId, Long initiatorId) {
        return EventRequest.builder()
                .factId(factId)
                .eventTypeId(eventTypeId)
                .initiatorId(initiatorId)
                .linkType(EVENT_DEFAULT_LINK)
                .build();
    }

    private Map<String, Object> convertProps(Map<String, Object> props, Map<String, ValueType> fieldTypes) {
        return props.entrySet()
                .stream()
                .peek(p -> p.setValue(convertService.convert(p.getValue(), fieldTypes.get(p.getKey()))))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
