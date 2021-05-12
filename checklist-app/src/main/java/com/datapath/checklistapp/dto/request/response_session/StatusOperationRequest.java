package com.datapath.checklistapp.dto.request.response_session;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusOperationRequest {

    @NotNull
    private Integer id;
    @NotNull
    private Integer statusId;
}
