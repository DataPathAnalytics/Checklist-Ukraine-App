package com.datapath.checklistukraineapp.dto.response;

import com.datapath.checklistukraineapp.dto.DepartmentDTO;
import lombok.Data;

import java.util.List;

@Data
public class MappingPublicResponse {

    private List<DepartmentDTO> departments;
}
