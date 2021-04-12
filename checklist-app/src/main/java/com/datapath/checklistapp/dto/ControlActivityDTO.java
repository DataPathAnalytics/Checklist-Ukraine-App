package com.datapath.checklistapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ControlActivityDTO {

    private Long id;
    private String name;
    private LocalDateTime dateCreated;
    private LocalDateTime dateModified;
    private Integer statusId;
    private Long authorId;
    private TemplateDTO templateConfig;

    private Set<Long> members;
    private List<TemplateDTO> templates;

    private List<AnswerDTO> answers;

    private SessionPageDTO sessions;
}
