package com.datapath.checklistapp.dto.request.activity;

import com.datapath.checklistapp.dto.AnswerDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateControlActivityRequest {

    @NotNull
    private Long templateConfigId;

    private String name;
    @NotNull
    private List<AnswerDTO> answers;

    private Set<Long> memberIds = new HashSet<>();

    private Set<Long> templateIds;
}
