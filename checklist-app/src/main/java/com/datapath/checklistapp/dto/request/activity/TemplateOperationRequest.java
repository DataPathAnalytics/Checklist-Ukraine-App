package com.datapath.checklistapp.dto.request.activity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateOperationRequest {

    @NotNull
    private Long id;
    @NotNull
    private Long templateId;
}
