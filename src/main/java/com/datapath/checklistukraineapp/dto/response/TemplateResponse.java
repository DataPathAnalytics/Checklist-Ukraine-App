package com.datapath.checklistukraineapp.dto.response;

import com.datapath.checklistukraineapp.dto.QuestionDTO;
import com.datapath.checklistukraineapp.dto.TemplateDTO;
import lombok.Data;

import java.util.List;

@Data
public class TemplateResponse {

    private TemplateDTO template;

    private QuestionDTO objectQuestion;
    private List<QuestionDTO> objectFeatureQuestions;
//    private List<QuestionDTO> factQuestions;
    private List<QuestionDTO> typeQuestions;

//    private List<GroupQuestionsDTO> groupQuestions;
}
