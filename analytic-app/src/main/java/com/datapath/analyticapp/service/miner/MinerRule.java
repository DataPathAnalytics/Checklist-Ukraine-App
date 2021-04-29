package com.datapath.analyticapp.service.miner;

import com.datapath.analyticapp.service.miner.config.Direction;
import com.datapath.analyticapp.service.miner.config.MinerRulePlace;
import lombok.Data;

@Data
public class MinerRule {
    private MinerRulePlace place;
    private String role;
    private Direction direction;
    private String linkType;
    private String secondNodeType;
    private String parentOfSecondNode;
    private String parentOfSecondNodeLinkType;
}
