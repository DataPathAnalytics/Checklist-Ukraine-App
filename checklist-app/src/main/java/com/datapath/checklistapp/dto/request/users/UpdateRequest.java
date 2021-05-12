package com.datapath.checklistapp.dto.request.users;

import com.datapath.checklistapp.dto.DepartmentDTO;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
public class UpdateRequest {

    @NotNull
    private Integer id;

    @Valid
    private DepartmentDTO department;
    private Boolean disable;
    private Integer permissionId;
}
