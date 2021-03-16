package com.datapath.checklistapp.dto.response.export;

import com.datapath.checklistapp.dto.QuestionExecutionDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ExportSessionDTO {
    private Long id;
    private String name;
    private LocalDateTime dateCreated;
    private LocalDateTime dateModified;
    private Long authorId;
    private Long reviewerId;

    private List<QuestionExecutionDTO> executions;
}
