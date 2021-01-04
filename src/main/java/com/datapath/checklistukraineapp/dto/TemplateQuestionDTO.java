package com.datapath.checklistukraineapp.dto;

import com.datapath.checklistukraineapp.util.Answer;
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
    private Answer conditionAnswer;
    private String groupName;
}
