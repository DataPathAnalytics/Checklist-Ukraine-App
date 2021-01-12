package com.datapath.checklistukraineapp.dto.request.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddTemplateRequest {

    @NotNull
    private Long id;
    @NotNull
    private Long templateId;
}
