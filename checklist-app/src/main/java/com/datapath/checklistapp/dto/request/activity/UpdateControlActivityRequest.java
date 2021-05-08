package com.datapath.checklistapp.dto.request.activity;

import com.datapath.checklistapp.dto.AnswerDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateControlActivityRequest {

    @NotNull
    private Integer id;
    @NotNull
    @Valid
    private List<AnswerDTO> answers;

    private List<Integer> memberIds = new ArrayList<>();
}
