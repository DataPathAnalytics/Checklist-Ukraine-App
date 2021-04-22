package com.datapath.checklistapp.dao.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExportSessionResponseDomain {
    private Long activityId;
    private Long sessionResponseId;
    private LocalDateTime dateModified;
}
