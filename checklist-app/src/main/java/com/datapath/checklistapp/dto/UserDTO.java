package com.datapath.checklistapp.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDTO {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private Integer permissionId;
    private DepartmentDTO department;
    private LocalDateTime registeredDate;
    private boolean disable;
    private boolean locked;
}
