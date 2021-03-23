package com.datapath.checklistapp.dto.request.question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateQuestionRequest {

    @NotBlank
    private String value;
    @NotNull
    private List<Long> knowledgeCategoryIds;

    private Long answerStructureId;
    private String questionSourceId;
    private String questionSourceParagraph;

    private List<String> linkTypes;

    @AssertTrue
    public Boolean isValid() {
        return !isEmpty(knowledgeCategoryIds) && !isEmpty(linkTypes);
    }
}
