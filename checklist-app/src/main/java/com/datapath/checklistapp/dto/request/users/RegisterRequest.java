package com.datapath.checklistapp.dto.request.users;

import com.datapath.checklistapp.dto.DepartmentDTO;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class RegisterRequest {

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String email;
    @NotBlank
    private String password;

    @NotNull
    @Valid
    private DepartmentDTO department;
}
