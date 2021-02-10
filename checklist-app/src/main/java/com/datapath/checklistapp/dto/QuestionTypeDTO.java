package com.datapath.checklistapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class QuestionTypeDTO {

    private Integer questionTypeId;
    private List<QuestionDTO> questions;
}
