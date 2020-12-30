package com.datapath.checklistukraineapp.domain.response;

import com.datapath.checklistukraineapp.domain.dto.PermissionDTO;
import lombok.Data;

import java.util.List;

@Data
public class MappingPrivateResponse {

    private List<PermissionDTO> permissions;
}
