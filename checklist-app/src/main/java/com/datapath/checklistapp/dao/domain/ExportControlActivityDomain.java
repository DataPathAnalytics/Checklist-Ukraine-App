package com.datapath.checklistapp.dao.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExportControlActivityDomain {
    private Long id;
    private Long activityResponseId;
    private LocalDateTime dateModified;
}
