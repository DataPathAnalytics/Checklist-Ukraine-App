package com.datapath.checklistapp.dto.request.response_session;

import com.datapath.checklistapp.dto.AnswerDTO;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
public class CreateRequest {

    @NotNull
    private Integer controlActivityId;
    @NotNull
    private Integer templateId;

    @Valid
    private List<AnswerDTO> answers = new ArrayList<>();

    private boolean autoCreated;
}
