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
    private String name;

    private String questionSourceId;
    private String questionSourceName;
    private String questionSourceLink;
    private String documentParagraph;

    private String groupName;
    private Long parentQuestionId;
    private Integer parentAnswerId;
}
