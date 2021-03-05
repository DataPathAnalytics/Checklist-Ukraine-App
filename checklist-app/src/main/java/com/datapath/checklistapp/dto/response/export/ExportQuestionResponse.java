package com.datapath.checklistapp.dto.response.export;

import com.datapath.checklistapp.dto.QuestionDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ExportQuestionResponse {

    private List<QuestionDTO> questions;
    private LocalDateTime lastDate;
}
