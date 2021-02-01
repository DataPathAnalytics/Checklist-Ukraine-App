package com.datapath.checklistukraineapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserPageDTO {
    private long totalCount;
    private int totalPageCount;
    private int currentPage;
    private int pageSize;
    private List<UserDTO> users;
}
