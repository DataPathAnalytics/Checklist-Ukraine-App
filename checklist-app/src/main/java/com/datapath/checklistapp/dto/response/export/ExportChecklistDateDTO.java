package com.datapath.checklistapp.dto.response.export;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExportChecklistDateDTO {
    private Long id;
    private LocalDateTime dateModified;
}
