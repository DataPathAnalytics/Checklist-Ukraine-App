package com.datapath.checklistapp.dto.response.mapping;

import com.datapath.checklistapp.dto.IdValueDTO;
import com.datapath.checklistapp.dto.PermissionDTO;
import lombok.Data;

import java.util.List;

@Data
public class MappingPrivateResponse {

    private List<PermissionDTO> permissions;
    private List<IdValueDTO> answerTypes;
    private List<IdValueDTO> activityStatuses;
    private List<IdValueDTO> sessionStatuses;
    private List<IdValueDTO> questionTypes;
    private List<IdValueDTO> templateConfigTypes;
}
