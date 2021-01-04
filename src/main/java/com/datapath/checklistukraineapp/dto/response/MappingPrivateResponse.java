package com.datapath.checklistukraineapp.dto.response;

import com.datapath.checklistukraineapp.dto.PermissionDTO;
import com.datapath.checklistukraineapp.util.Answer;
import lombok.Data;

import java.util.List;

@Data
public class MappingPrivateResponse {

    private List<PermissionDTO> permissions;
    private Answer[] answers;
}
