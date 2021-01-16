package com.datapath.checklistukraineapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static java.util.Objects.nonNull;

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

    @NotBlank
    private String groupName;
    private Long parentQuestionId;
    private Integer parentAnswerId;

    @AssertTrue
    public boolean parentCondition() {
        if (nonNull(parentQuestionId)) {
            return nonNull(parentAnswerId);
        }
        return true;
    }
}
