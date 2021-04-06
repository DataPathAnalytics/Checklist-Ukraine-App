package com.datapath.checklistapp.dto.request.question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateQuestionRequest {

    @NotBlank
    private String value;
    @NotEmpty
    private List<Long> knowledgeCategoryIds;

    private Long answerStructureId;
    private String questionSourceName;
    private String questionSourceLink;

    @AssertTrue
    public Boolean isValid() {
        return !isEmpty(knowledgeCategoryIds);
    }
}
