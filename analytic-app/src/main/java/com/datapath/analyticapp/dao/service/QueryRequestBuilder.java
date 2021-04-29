package com.datapath.analyticapp.dao.service;

import com.datapath.analyticapp.dao.service.request.*;
import com.datapath.analyticapp.dto.imported.response.ValueType;
import com.datapath.analyticapp.service.miner.MinerRule;
import lombok.Data;

import java.util.List;
import java.util.Map;

import static com.datapath.analyticapp.Constants.*;

@Data
public class QueryRequestBuilder {

    public static IdentifierRequest identifierRequest(String nodeType,
                                                      String identifierField,
                                                      Object identifierValue,
                                                      ValueType valueType,
                                                      Map<String, Object> props) {
        return IdentifierRequest.builder()
                .nodeType(nodeType)
                .identifierField(identifierField)
                .identifierValue(identifierValue)
                .identifierType(valueType)
                .props(props)
                .build();
    }

    public static NonIdentifierRequest nonIdentifierRequest(Long parentId,
                                                            String nodeType,
                                                            String linkType,
                                                            Map<String, Object> props) {
        return NonIdentifierRequest.builder()
                .nodeType(nodeType)
                .parentId(parentId)
                .linkType(linkType)
                .props(props)
                .build();
    }

    public static RelationshipRequest relationshipRequest(Long parentId,
                                                          Long childId,
                                                          String linkType) {
        return RelationshipRequest.builder()
                .parentId(parentId)
                .childId(childId)
                .linkType(linkType)
                .build();
    }

    public static RuleRelationshipRequest ruleRelationshipRequest(MinerRule rule,
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

    public static FactRequest factRequest(Long parentId,
                                          Object value,
                                          String valueName,
                                          ValueType valueType,
                                          String questionValue
    ) {
        return FactRequest.builder()
                .parentId(parentId)
                .value(value)
                .valueName(valueName)
                .valueType(valueType)
                .questionValue(questionValue)
                .nodeType(FACT_DEFAULT_NODE)
                .linkType(FACT_DEFAULT_LINK)
                .build();
    }

    public static DeleteRequest deleteRequest(Long parentNodeId,
                                              String nodeType,
                                              String linkType) {
        return DeleteRequest.builder()
                .parentId(parentNodeId)
                .nodeType(nodeType)
                .linkType(linkType)
                .build();
    }

    public static EventRequest eventRequest(Long parentNodeId, Long eventTypeId) {
        return EventRequest.builder()
                .parentId(parentNodeId)
                .eventTypeId(eventTypeId)
                .nodeType(EVENT_DEFAULT_NODE)
                .linkType(EVENT_DEFAULT_LINK)
                .build();
    }
}
