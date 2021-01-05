package com.datapath.checklistukraineapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserStateDTO {
    private boolean existsNotCheckedUsers;
}
