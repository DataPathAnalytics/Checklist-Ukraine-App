package com.datapath.checklistapp.dto.request.activity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseSessionStatusRequest {

    @NotNull
    private Long controlActivityId;
    @NotNull
    private Long responseSessionId;
    @NotNull
    private Integer sessionStatusId;
}
