package com.datapath.checklistukraineapp.dto.request.event;

import com.datapath.checklistukraineapp.dto.ChecklistAnswerDTO;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class SaveChecklistRequest {

    private Long id;
    @NotBlank
    private String name;
    @NotNull
    private Long eventId;
    @NotNull
    private Long templateId;

    private List<ChecklistAnswerDTO> answers;
}
