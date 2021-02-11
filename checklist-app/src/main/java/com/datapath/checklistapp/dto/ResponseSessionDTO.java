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

    private QuestionExecutionDTO objectQuestion;
    private List<QuestionExecutionDTO> objectFeatureQuestions;
    private List<QuestionExecutionDTO> typeQuestions;
    private List<GroupQuestionsDTO> questions;
    private List<QuestionExecutionDTO> ungroupedQuestions;
}
