package com.datapath.checklistukraineapp.domain.request.users;

import com.datapath.checklistukraineapp.util.UserRole;
import lombok.Data;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

import static java.util.Objects.isNull;

@Data
public class UserUpdateRequest {

    @NotNull
    private Long id;
    private String department;
    private Boolean disable;
    private UserRole userRole;

    @AssertTrue(message = "You can assign only 'auditor' or 'methodologist' role")
    public boolean isValidRole() {
        return isNull(userRole) ||
                UserRole.auditor.equals(userRole) ||
                UserRole.methodologist.equals(userRole);
    }
}
