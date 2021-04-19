package com.datapath.checklistapp.dao.domain;

import lombok.Data;

@Data
public class ExportResponseSessionDomain {
    private Long sessionResponseId;
    private Long activityId;
    private Long activityResponseId;

}
