package com.datapath.checklistapp.dto.response.export;

import com.datapath.checklistapp.dto.ResponseSessionDTO;
import lombok.Data;

@Data
public class ExportResponseSessionDTO {
    private Long controlActivityId;
    private ResponseSessionDTO session;
}
