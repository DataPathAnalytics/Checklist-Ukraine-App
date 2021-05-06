package com.datapath.checklistapp.dto.response.export;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ExportChecklistDateResponse {

    private List<ExportChecklistDateDTO> data;
    private LocalDateTime nextOffset;
}