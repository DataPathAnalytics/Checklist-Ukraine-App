package com.datapath.checklistukraineapp.dto.request.users;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserUpdateRequest {

    @NotNull
    private Long id;
    private Long departmentId;
    private Boolean disable;
    private Long permissionId;
}
