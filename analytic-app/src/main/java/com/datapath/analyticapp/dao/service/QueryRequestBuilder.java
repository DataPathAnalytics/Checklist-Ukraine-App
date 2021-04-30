package com.datapath.analyticapp.dao.service;

import com.datapath.analyticapp.dao.service.request.*;
import com.datapath.analyticapp.dto.imported.response.ValueType;
import com.datapath.analyticapp.service.miner.MinerRule;
import com.datapath.analyticapp.service.miner.converter.ConvertTypeService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.datapath.analyticapp.Constants.*;

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
                                                     Map<String, ValueType> fieldTypes) {
        return NonIdentifierRequest.builder()
                .nodeType(nodeType)
                .parentId(parentId)
                .linkType(linkType)
                .props(convertProps(props, fieldTypes))
                .fieldTypes(fieldTypes)
                .build();
    }

    public RelationshipRequest relationshipRequest(Long parentId,
                                                   Long childId,
                                                   String linkType) {
        return RelationshipRequest.builder()
                .parentId(parentId)
                .childId(childId)
                .linkType(linkType)
                .build();
    }

    public RuleRelationshipRequest ruleRelationshipRequest(MinerRule rule,
                                                           Long nodeId,
                                                           List<Long> secondNodeIds,
                                                           List<Long> parentOfSecondNodeIds,
                                                           String parentOfSecondNodeLink) {
        return RuleRelationshipRequest.builder()
                .rule(rule)
                .nodeId(nodeId)
                .secondNodeIds(secondNodeIds)
                .parentOfSecondNodeIds(parentOfSecondNodeIds)
                .parentOfSecondNodeLink(parentOfSecondNodeLink)
                .build();
    }

    public FactRequest factRequest(Long parentId,
                                   String fieldName,
                                   String questionValue,
                                   Map<String, Object> props,
                                   Map<String, ValueType> fieldTypes) {
        return FactRequest.builder()
                .parentId(parentId)
                .fieldName(fieldName)
                .questionValue(questionValue)
                .props(convertProps(props, fieldTypes))
                .nodeType(FACT_DEFAULT_NODE)
                .linkType(FACT_DEFAULT_LINK)
                .build();
    }

    public DeleteRequest deleteRequest(Long parentNodeId,
                                       String nodeType,
                                       String linkType) {
        return DeleteRequest.builder()
                .parentId(parentNodeId)
                .nodeType(nodeType)
                .linkType(linkType)
                .build();
    }

    public EventRequest eventRequest(Long parentNodeId, Long eventTypeId) {
        return EventRequest.builder()
                .parentId(parentNodeId)
                .eventTypeId(eventTypeId)
                .nodeType(EVENT_DEFAULT_NODE)
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
