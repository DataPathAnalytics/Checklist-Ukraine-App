package com.datapath.analyticapp.dto.imported.user;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDateTime registeredDate;
    private LocalDateTime dateModified;
}
