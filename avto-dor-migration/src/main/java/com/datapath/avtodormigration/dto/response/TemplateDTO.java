package com.datapath.avtodormigration.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class TemplateDTO {
    private Integer id;
    private QuestionExecutionDTO objectQuestion;
    private QuestionExecutionDTO authorityQuestion;
    private List<QuestionExecutionDTO> objectFeatureQuestions;
    private List<QuestionExecutionDTO> ungroupedQuestions;
    private List<GroupQuestionsDTO> questionGroups;
    private List<QuestionExecutionDTO> typeQuestions;
    private List<QuestionExecutionDTO> authorityFeatureQuestions;
}
