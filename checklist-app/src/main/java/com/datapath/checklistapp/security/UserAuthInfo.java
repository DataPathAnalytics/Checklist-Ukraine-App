package com.datapath.checklistapp.security;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserAuthInfo {
    private String id;
    private String email;
}
