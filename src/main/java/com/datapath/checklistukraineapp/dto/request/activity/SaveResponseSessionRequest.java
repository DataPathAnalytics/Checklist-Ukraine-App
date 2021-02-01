package com.datapath.checklistukraineapp.dto.request.activity;

import com.datapath.checklistukraineapp.dto.AnswerDTO;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class SaveResponseSessionRequest {

    private Long id;
    @NotBlank
    private String name;

    @NotNull
    private Long controlActivityId;
    @NotNull
    private Long templateId;

    private List<AnswerDTO> answers;
}
