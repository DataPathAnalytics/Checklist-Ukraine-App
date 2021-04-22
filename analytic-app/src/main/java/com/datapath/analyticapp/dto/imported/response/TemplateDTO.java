package com.datapath.analyticapp.dto.imported.response;

import lombok.Data;

import java.util.List;

@Data
public class TemplateDTO {
    private QuestionExecutionDTO objectQuestion;
    private QuestionExecutionDTO authorityQuestion;
    private List<QuestionExecutionDTO> objectFeatureQuestions;
    private List<QuestionExecutionDTO> authorityFeatureQuestions;
    private List<QuestionExecutionDTO> typeQuestions;
    private List<QuestionExecutionDTO> ungroupedQuestions;
    private List<QuestionGroupDTO> questionGroups;

    @Data
    public static class QuestionGroupDTO {
        private List<QuestionExecutionDTO> questions;
    }
}
