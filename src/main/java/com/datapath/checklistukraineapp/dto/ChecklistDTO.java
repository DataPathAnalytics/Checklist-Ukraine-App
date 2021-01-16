package com.datapath.checklistukraineapp.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ChecklistDTO {
    private Long id;
    private Integer checklistStatusId;
    private String name;
    private LocalDate dateCreated;
    private Long templateId;
    private String templateName;
    private Long reviewerId;
    private Long authorId;

    private List<QuestionAnswerDTO> answers;
}
