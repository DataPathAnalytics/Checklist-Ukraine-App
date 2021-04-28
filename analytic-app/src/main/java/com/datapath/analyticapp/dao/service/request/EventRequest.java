package com.datapath.analyticapp.dao.service.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventRequest {
    private Long parentId;
    private Long eventTypeId;
    private String nodeType;
    private String linkType;
}
