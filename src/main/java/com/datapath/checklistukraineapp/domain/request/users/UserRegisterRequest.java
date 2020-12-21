package com.datapath.checklistukraineapp.domain.request.users;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserRegisterRequest {

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String department;
}
