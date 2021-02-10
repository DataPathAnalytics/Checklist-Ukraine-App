package com.datapath.checklistapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserStateDTO {
    private boolean existsNotCheckedUsers;
}
