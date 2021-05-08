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
    private Integer controlActivityId;
    @NotNull
    private Integer responseSessionId;
    @NotNull
    private Integer sessionStatusId;
}
