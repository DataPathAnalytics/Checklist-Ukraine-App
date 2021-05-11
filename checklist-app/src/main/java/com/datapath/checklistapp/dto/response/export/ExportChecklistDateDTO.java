package com.datapath.checklistapp.dto.response.export;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExportChecklistDateDTO {
    private Integer id;
    private LocalDateTime dateModified;
}
