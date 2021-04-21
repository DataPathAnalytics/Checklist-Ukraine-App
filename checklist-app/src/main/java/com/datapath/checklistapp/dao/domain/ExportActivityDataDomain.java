package com.datapath.checklistapp.dao.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExportActivityDataDomain {
    private Long id;
    private Long activityResponseId;
    private Long sessionResponseId;
    private LocalDateTime dateModified;
}
