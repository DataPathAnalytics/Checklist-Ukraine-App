package com.datapath.checklistukraineapp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateQuestionRequest {

    @NotBlank
    private String name;
    @NotNull
    private String questionSourceId;
    @NotBlank
    private String questionSourceParagraph;
}
