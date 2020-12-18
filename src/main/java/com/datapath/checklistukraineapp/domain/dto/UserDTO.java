package com.datapath.checklistukraineapp.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDTO {

    private Long id;
    private String email;
    private String name;
    private String role;
    private Long departmentId;
    private LocalDateTime registeredDateTime;
    private boolean disable;
    private boolean locked;
}
