package com.datapath.checklistukraineapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemplateDTO {

    private Long id;
    private String name;
    private LocalDate dateCreated;
    private Long authorId;
    private Long folderId;
    private Long templateConfigId;
    private Integer templateType;

    private QuestionExecutionDTO objectQuestion;
    private List<QuestionExecutionDTO> objectFeatureQuestions;
    private List<QuestionExecutionDTO> ungroupedQuestions;
    private List<GroupQuestionsDTO> questions;
    private List<QuestionExecutionDTO> typeQuestions;
}
