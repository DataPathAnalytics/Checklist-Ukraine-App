package com.datapath.checklistukraineapp.domain.request.users;

import lombok.Data;

@Data
public class LoginRequest {

    private String email;
    private String password;

}
