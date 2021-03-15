package com.datapath.checklistapp.dto.request.question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

import static java.util.Objects.nonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateQuestionRequest {

    @NotBlank
    private String name;
    @NotNull
    private List<Long> knowledgeCategoryIds;
    @NotNull
    private Integer questionTypeId;

    private Long answerStructureId;
    private String questionSourceId;
    private String questionSourceParagraph;

    @AssertTrue
    public Boolean isValid() {
        if (questionTypeId != 3) {
            return nonNull(answerStructureId) && knowledgeCategoryIds.size() == 1;
        } else {
            return knowledgeCategoryIds.size() > 0;
        }
    }
}
