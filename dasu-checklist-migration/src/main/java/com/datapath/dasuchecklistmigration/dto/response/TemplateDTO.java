package com.datapath.dasuchecklistmigration.dto.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TemplateDTO {
    private Integer id;
    private QuestionExecutionDTO objectQuestion;
    private QuestionExecutionDTO authorityQuestion;
    private List<QuestionExecutionDTO> objectFeatureQuestions = new ArrayList<>();
    private List<QuestionExecutionDTO> ungroupedQuestions = new ArrayList<>();
    private List<GroupQuestionsDTO> questionGroups = new ArrayList<>();
    private List<QuestionExecutionDTO> typeQuestions = new ArrayList<>();
    private List<QuestionExecutionDTO> authorityFeatureQuestions = new ArrayList<>();
}
