package com.datapath.checklistapp.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class UserDTO {

    private Integer id;
    private String email;
    private String firstName;
    private String lastName;
    private Set<PermissionDTO> permissions;
    private DepartmentDTO department;
    private LocalDateTime registeredDate;
    private boolean disable;
    private boolean locked;
}
