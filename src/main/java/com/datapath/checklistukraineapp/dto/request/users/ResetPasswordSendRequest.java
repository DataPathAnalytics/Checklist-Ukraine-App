package com.datapath.checklistukraineapp.dto.request.users;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ResetPasswordSendRequest {

    @NotBlank
    private String path;
    @NotBlank
    private String email;

}
