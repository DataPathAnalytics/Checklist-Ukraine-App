package com.datapath.checklistapp.dto.request.activity;

import com.datapath.checklistapp.dto.AnswerDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateControlActivityRequest {

    @NotNull
    private Long templateConfigId;

    private String name;
    @NotNull
    private List<AnswerDTO> answers;

    private List<Long> memberIds = new ArrayList<>();

    private List<Long> templateIds;
}
