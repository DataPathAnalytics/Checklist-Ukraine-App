package com.datapath.checklistapp.dto.response.export;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ExportControlActivityDTO {

    private Long id;
    private String name;
    private Long authorId;
    private LocalDateTime dateCreated;
    private List<Long> memberIds;

    private List<ExportSessionDTO> sessions;
}
