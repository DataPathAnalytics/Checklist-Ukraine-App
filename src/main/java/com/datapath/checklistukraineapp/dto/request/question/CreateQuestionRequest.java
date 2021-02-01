package com.datapath.checklistukraineapp.dto.request.question;

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
public class CreateQuestionRequest {

    @NotBlank
    private String name;
    @NotNull
    private Integer knowledgeCategoryId;
    @NotNull
    private Integer questionTypeId;
    @NotNull
    private Long answerStructureId;
    @NotNull
    private String questionSourceId;
    @NotNull
    private String questionSourceParagraph;

    @AssertTrue
    public boolean isValid() {
        if (questionTypeId != 3) return nonNull(answerStructureId);
        return true;
    }
}