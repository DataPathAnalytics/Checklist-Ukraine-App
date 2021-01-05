package com.datapath.checklistukraineapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemplateQuestionDTO {

    @NotNull
    private Long questionId;
    private Long parentQuestionId;
    private Integer answerId;
    private String groupName;
}
