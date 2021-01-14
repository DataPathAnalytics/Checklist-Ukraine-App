package com.datapath.checklistukraineapp.dto.request.event;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CreateChecklistRequest {

    @NotBlank
    private String name;
    @NotNull
    private Long eventId;
    @NotNull
    private Long templateId;
    @NotNull
    private Long reviewerId;

}
