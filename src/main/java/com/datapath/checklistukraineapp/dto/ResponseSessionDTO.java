package com.datapath.checklistukraineapp.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ResponseSessionDTO {

    private Long id;
    private Integer sessionStatusId;
    private String name;
    private LocalDate dateCreated;
    private Long templateId;
    private String templateName;
    private Long reviewerId;
    private Long authorId;

    private QuestionExecutionDTO objectQuestion;
    private List<QuestionExecutionDTO> objectFeatureQuestions;
    private List<QuestionExecutionDTO> typeQuestions;
    private List<GroupQuestionsDTO> questions;
    private List<QuestionExecutionDTO> ungroupedQuestions;
}
