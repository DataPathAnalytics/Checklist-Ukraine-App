package com.datapath.checklistukraineapp.domain.response;

import com.datapath.checklistukraineapp.domain.dto.DepartmentDTO;
import lombok.Data;

import java.util.List;

@Data
public class MappingPublicResponse {

    private List<DepartmentDTO> departments;
}
