package com.datapath.analyticapp.dao.service.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeleteRequest {
    private Long parentId;
    private String nodeType;
    private String linkType;
}
