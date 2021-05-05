package com.datapath.analyticapp.dto.imported.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChecklistDateDTO {
    private Long id;
    private LocalDateTime dateModified;
}
