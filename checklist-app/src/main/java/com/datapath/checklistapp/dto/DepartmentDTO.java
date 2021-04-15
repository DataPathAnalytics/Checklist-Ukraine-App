package com.datapath.checklistapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class DepartmentDTO {

    @NotNull
    private Long identifier;
    private String name;
}
