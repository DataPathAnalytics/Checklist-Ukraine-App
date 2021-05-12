package com.datapath.checklistapp.dto.request.response_session;

import com.datapath.checklistapp.dto.AnswerDTO;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
public class UpdateRequest {

    @NotNull
    private Integer id;
    @Valid
    private List<AnswerDTO> answers = new ArrayList<>();
}
