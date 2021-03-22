package com.datapath.checklistapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemplateDTO {

    private Long id;
    private String name;
    private LocalDateTime dateCreated;
    private Long authorId;
    private Long folderId;
    private Long templateConfigId;
    private Integer templateConfigTypeId;

    private QuestionExecutionDTO objectQuestion;
    private List<QuestionExecutionDTO> featureQuestions;
    private List<QuestionExecutionDTO> ungroupedQuestions;
    private List<GroupQuestionsDTO> questions;
    private List<QuestionExecutionDTO> typeQuestions;
    private List<QuestionExecutionDTO> authorityQuestions;
}
