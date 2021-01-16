package com.datapath.checklistukraineapp.dto.request.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChecklistStatusRequest {

    @NotNull
    private Long id;
    @NotNull
    private Integer checklistStatusId;
}
