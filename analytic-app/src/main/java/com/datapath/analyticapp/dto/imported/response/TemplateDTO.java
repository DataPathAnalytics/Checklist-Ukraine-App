package com.datapath.analyticapp.dto.imported.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TemplateDTO {
    private QuestionExecutionDTO objectQuestion;
    private QuestionExecutionDTO authorityQuestion;
    private List<QuestionExecutionDTO> objectFeatureQuestions = new ArrayList<>();
    private List<QuestionExecutionDTO> authorityFeatureQuestions = new ArrayList<>();
    private List<QuestionExecutionDTO> typeQuestions = new ArrayList<>();
    private List<QuestionExecutionDTO> ungroupedQuestions = new ArrayList<>();
    private List<QuestionGroupDTO> questionGroups = new ArrayList<>();

    @Data
    public static class QuestionGroupDTO {
        private List<QuestionExecutionDTO> questions = new ArrayList<>();
    }
}
