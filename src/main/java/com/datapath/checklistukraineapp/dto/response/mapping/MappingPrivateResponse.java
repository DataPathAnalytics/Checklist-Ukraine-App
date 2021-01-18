package com.datapath.checklistukraineapp.dto.response.mapping;

import com.datapath.checklistukraineapp.dto.*;
import lombok.Data;

import java.util.List;

@Data
public class MappingPrivateResponse {

    private List<PermissionDTO> permissions;
    private List<AnswerDTO> answers;
    private List<ControlTypeDTO> controlTypes;
    private List<ControlStatusDTO> controlStatuses;
    private List<FinancingTypeDTO> financingTypes;
    private List<FinancingDirectionDTO> financingDirections;
}
