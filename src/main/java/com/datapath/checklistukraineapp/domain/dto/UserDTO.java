package com.datapath.checklistukraineapp.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDTO {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private String department;
    private LocalDateTime registeredDateTime;
    private boolean disable;
    private boolean locked;
}
