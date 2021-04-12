package com.datapath.checklistapp.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ResponseSessionDTO {

    private Long id;
    private Integer sessionStatusId;
    private String name;
    private LocalDateTime dateCreated;
    private Long templateId;
    private String templateName;
    private Long reviewerId;
    private Long authorId;

    private TemplateDTO template;

    private List<AnswerDTO> answers;
}
