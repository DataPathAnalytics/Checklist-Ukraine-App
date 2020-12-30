package com.datapath.checklistukraineapp.domain.request.template;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTemplateRequest {

    @NotBlank
    private String name;
    @NotNull
    private Long folderId;
}
