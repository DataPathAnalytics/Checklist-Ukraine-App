package com.datapath.checklistapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PermissionDTO {
    private Integer id;
    private String role;
    private String value;
}
