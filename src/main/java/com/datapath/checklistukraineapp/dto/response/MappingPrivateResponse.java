package com.datapath.checklistukraineapp.dto.response;

import com.datapath.checklistukraineapp.dto.AnswerDTO;
import com.datapath.checklistukraineapp.dto.ControlTypeDTO;
import com.datapath.checklistukraineapp.dto.PermissionDTO;
import lombok.Data;

import java.util.List;

@Data
public class MappingPrivateResponse {

    private List<PermissionDTO> permissions;
    private List<AnswerDTO> answers;
    private List<ControlTypeDTO> controlTypes;
}
