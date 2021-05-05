package com.datapath.analyticapp.dto.imported.response;

import lombok.Data;

@Data
public class ControlActivitySessionDTO {
    private Long controlActivityId;
    private SessionDTO session;
}
