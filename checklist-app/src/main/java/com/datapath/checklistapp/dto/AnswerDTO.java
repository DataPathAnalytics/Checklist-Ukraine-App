package com.datapath.checklistapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerDTO {

    private Integer id;
    @NotNull
    private Integer questionId;

    private String comment;

    private Map<String, Object> values;
}
