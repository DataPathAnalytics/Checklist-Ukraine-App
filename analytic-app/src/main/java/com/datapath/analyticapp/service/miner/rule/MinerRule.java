package com.datapath.analyticapp.service.miner.rule;

import com.datapath.analyticapp.service.miner.config.Direction;
import com.datapath.analyticapp.service.miner.config.Place;
import lombok.Data;

@Data
public class MinerRule {
    private Place place;
    private String role;
    private Direction direction;
    private String linkType;
    private String secondNodeType;
    private String parentOfSecondNode;
    private String parentOfSecondNodeLinkType;
}
