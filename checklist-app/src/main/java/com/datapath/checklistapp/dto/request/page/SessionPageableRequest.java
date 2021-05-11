package com.datapath.checklistapp.dto.request.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionPageableRequest {
    @NotNull
    private Integer controlActivityId;
    private int page = 0;
    private int size = 10;
}
