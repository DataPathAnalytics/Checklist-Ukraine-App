package com.datapath.checklistapp.dto.request.activity;

import com.datapath.checklistapp.dto.AnswerDTO;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class SaveResponseSessionRequest {

    private Integer id;

    @NotNull
    private Integer controlActivityId;
    @NotNull
    private Integer templateId;

    @Valid
    private List<AnswerDTO> answers;

    private boolean autoCreated;
}
