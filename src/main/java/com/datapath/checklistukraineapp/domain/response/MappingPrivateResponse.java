package com.datapath.checklistukraineapp.domain.response;

import com.datapath.checklistukraineapp.util.UserRole;
import lombok.Data;

import java.util.List;

@Data
public class MappingPrivateResponse {

    private List<UserRole> roles;
}
