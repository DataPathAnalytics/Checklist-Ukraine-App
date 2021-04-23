package com.datapath.analyticapp.service.db;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParentNodeData {
    private String nodeType;
    private Long id;
}
