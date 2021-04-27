package com.datapath.analyticapp.dao.service;

import com.datapath.analyticapp.dto.imported.response.ValueType;
import com.datapath.analyticapp.service.miner.MinerRule;
import com.datapath.analyticapp.service.miner.ValueTypeConverter;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

import static com.datapath.analyticapp.Constants.*;

@Data
@Builder
public class QueryRequest {

    private String nodeType;
    private String identifierField;
    private Object identifierValue;
    private ValueType identifierType;

    private Long parentId;
    private Long childId;

    private String linkType;
    private Long answerId;

    private Map<String, Object> props;

    private MinerRule rule;
    private Long nodeId;
    private List<Long> secondNodeIds;
    private List<Long> parentOfSecondNodeIds;
    private String parentOfSecondNodeLink;

    private Long eventTypeId;

    public String getIdentifierValue() {
        return ValueTypeConverter.convert(identifierValue, identifierType);
    }

    public static QueryRequest forIdentifier(String nodeType,
                                             String identifierField,
                                             Object identifierValue,
                                             ValueType valueType,
                                             Map<String, Object> props) {
        return QueryRequest.builder()
                .nodeType(nodeType)
                .identifierField(identifierField)
                .identifierValue(identifierValue)
                .identifierType(valueType)
                .props(props)
                .build();
    }

    public static QueryRequest forNonIdentifier(Long parentId,
                                                Long answerId,
                                                String nodeType,
                                                String linkType,
                                                Map<String, Object> props) {
        return QueryRequest.builder()
                .nodeType(nodeType)
                .parentId(parentId)
                .answerId(answerId)
                .linkType(linkType)
                .props(props)
                .build();
    }

    public static QueryRequest forRelationship(Long parentId,
                                               Long childId,
                                               String linkType) {
        return QueryRequest.builder()
                .parentId(parentId)
                .childId(childId)
                .linkType(linkType)
                .build();
    }

    public static QueryRequest forRuleRelationship(MinerRule rule,
                                                   Long nodeId,
                                                   List<Long> secondNodeIds,
                                                   List<Long> parentOfSecondNodeIds,
                                                   String parentOfSecondNodeLink) {
        return QueryRequest.builder()
                .rule(rule)
                .nodeId(nodeId)
                .secondNodeIds(secondNodeIds)
                .parentOfSecondNodeIds(parentOfSecondNodeIds)
                .parentOfSecondNodeLink(parentOfSecondNodeLink)
                .build();
    }

    public static QueryRequest forFactNode(Long parentId,
                                           Long answerId,
                                           Map<String, Object> props) {
        return QueryRequest.builder()
                .parentId(parentId)
                .answerId(answerId)
                .nodeType(FACT_DEFAULT_NODE)
                .linkType(FACT_DEFAULT_LINK)
                .props(props)
                .build();
    }

    public static QueryRequest forEventNode(Long parentNodeId) {
        return QueryRequest.builder()
                .parentId(parentNodeId)
                .nodeType(EVENT_DEFAULT_NODE)
                .linkType(EVENT_DEFAULT_LINK)
                .build();
    }
}
