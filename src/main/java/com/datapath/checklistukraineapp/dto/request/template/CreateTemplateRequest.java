package com.datapath.checklistukraineapp.dto.request.template;

import com.datapath.checklistukraineapp.dto.TemplateQuestionDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTemplateRequest {

    @NotBlank
    private String name;
    @NotNull
    private Long folderId;
    @NotNull
    private Long subjectTypeId;
    @NotNull
    private Long ownerTypeId;
    @NotNull
    private List<TemplateQuestionDTO> factQuestions;
    @NotNull
    private List<Long> ontologyQuestions;
}
