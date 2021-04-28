package com.datapath.analyticapp.dao.service.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RelationshipRequest {
    private Long parentId;
    private Long childId;
    private String linkType;
}
