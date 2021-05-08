package com.datapath.checklistapp.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ResponseSessionDTO {

    private Integer id;
    private Integer sessionStatusId;
    private Integer number;
    private LocalDateTime dateCreated;
    private LocalDateTime dateModified;

    private Integer authorId;
    private Integer reviewerId;
    private List<Integer> members;

    private TemplateDTO template;

    private List<AnswerDTO> answers;

    private boolean invalid;
}
