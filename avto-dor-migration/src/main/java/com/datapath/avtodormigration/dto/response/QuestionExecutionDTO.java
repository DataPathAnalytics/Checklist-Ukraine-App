package com.datapath.avtodormigration.dto.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class QuestionExecutionDTO {

    private Integer id;
    private QuestionDTO question;
    private List<SubQuestionDTO> subQuestions = new ArrayList<>();
    private boolean root;
}
