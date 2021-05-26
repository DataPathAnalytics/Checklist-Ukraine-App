package com.datapath.dasuchecklistmigration.dto.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GroupQuestionsDTO {

    private String groupName;
    private Integer orderNumber;
    private List<QuestionExecutionDTO> questions = new ArrayList<>();
}
