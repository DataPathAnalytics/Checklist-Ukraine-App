package com.datapath.checklistukraineapp.domain.request.users;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ResetPasswordRequest {

    @NotNull
    @NotEmpty
    private String token;

    @NotNull
    @NotEmpty
    private String password;
}
