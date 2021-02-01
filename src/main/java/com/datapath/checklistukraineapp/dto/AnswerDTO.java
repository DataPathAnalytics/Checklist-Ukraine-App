package com.datapath.checklistukraineapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerDTO {

    @NotNull
    private Long questionId;

    private Integer answerTypeId;
    private String comment;

    private Map<String, Object> values;
}