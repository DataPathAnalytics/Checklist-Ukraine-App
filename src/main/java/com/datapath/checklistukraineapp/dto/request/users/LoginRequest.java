package com.datapath.checklistukraineapp.dto.request.users;

import lombok.Data;

@Data
public class LoginRequest {

    private String email;
    private String password;

}
