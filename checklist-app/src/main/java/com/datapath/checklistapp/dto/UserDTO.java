package com.datapath.checklistapp.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDTO {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private Integer permissionId;
    private Long departmentId;
    private LocalDate registeredDate;
    private boolean disable;
    private boolean locked;
}
