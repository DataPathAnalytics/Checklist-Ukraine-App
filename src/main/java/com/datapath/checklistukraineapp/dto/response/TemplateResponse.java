package com.datapath.checklistukraineapp.dto.response;

import com.datapath.checklistukraineapp.dto.TemplateDTO;
import com.datapath.checklistukraineapp.dto.TemplateQuestionDTO;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TemplateResponse {

    private TemplateDTO template;
    private Map<String, List<TemplateQuestionDTO>> questions;
}
