package com.datapath.analyticapp.dao.service.request;

import com.datapath.analyticapp.service.miner.rule.MinerRule;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RuleRelationshipRequest {
    private MinerRule rule;
    private Long nodeId;
    private List<Long> secondNodeIds;
    private List<Long> parentOfSecondNodeIds;
    private String parentOfSecondNodeLink;
    private Long initiatorId;
}
