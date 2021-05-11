package com.datapath.checklistapp.dao.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExportSessionResponseDomain {
    private Integer id;
    private LocalDateTime dateModified;
}
