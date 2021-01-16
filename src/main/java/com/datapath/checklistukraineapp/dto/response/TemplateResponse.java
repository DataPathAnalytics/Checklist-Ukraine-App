package com.datapath.checklistukraineapp.dto.response;

import com.datapath.checklistukraineapp.dto.GroupQuestionsDTO;
import com.datapath.checklistukraineapp.dto.TemplateDTO;
import lombok.Data;

import java.util.List;

@Data
public class TemplateResponse {

    private TemplateDTO template;
    private List<GroupQuestionsDTO> groupQuestions;
}
