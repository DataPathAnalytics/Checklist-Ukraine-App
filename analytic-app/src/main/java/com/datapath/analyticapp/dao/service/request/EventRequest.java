package com.datapath.analyticapp.dao.service.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventRequest {
    private Long factId;
    private Long eventTypeId;
    private String linkType;
    private Long initiatorId;
}
