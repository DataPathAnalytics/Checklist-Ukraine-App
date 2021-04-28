package com.datapath.analyticapp.dao.service.request;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class NonIdentifierRequest {
    private Long parentId;
    private Long answerId;
    private String nodeType;
    private String linkType;
    private Map<String, Object> props;
}
