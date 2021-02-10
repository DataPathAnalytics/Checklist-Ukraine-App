package com.datapath.checklistapp.dto.response.mapping;

import com.datapath.checklistapp.dto.DepartmentDTO;
import lombok.Data;

import java.util.List;

@Data
public class MappingPublicResponse {

    private List<DepartmentDTO> departments;
}
