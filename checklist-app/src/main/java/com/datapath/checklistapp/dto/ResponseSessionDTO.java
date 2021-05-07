package com.datapath.checklistapp.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ResponseSessionDTO {

    private Long id;
    private Integer sessionStatusId;
    private Integer number;
    private LocalDateTime dateCreated;
    private LocalDateTime dateModified;

    private Long authorId;
    private Long reviewerId;
    private List<Long> members;

    private TemplateDTO template;

    private List<AnswerDTO> answers;

    private boolean invalid;
}
