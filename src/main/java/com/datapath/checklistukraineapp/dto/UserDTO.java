package com.datapath.checklistukraineapp.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDTO {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private Long permissionId;
    private Long departmentId;
    private LocalDateTime registeredDateTime;
    private boolean disable;
    private boolean locked;
}
