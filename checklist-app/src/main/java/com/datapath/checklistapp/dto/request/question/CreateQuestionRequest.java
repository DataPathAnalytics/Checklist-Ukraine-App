package com.datapath.checklistapp.dto.request.question;

import com.datapath.checklistapp.dto.QuestionSourceDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateQuestionRequest {

    @NotBlank
    private String value;
    @NotEmpty
    private List<Long> knowledgeClassIds;

    private Long answerStructureId;

    @Valid
    private QuestionSourceDTO source;
}
