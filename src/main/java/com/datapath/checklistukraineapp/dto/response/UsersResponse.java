package com.datapath.checklistukraineapp.dto.response;

import com.datapath.checklistukraineapp.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UsersResponse {
    private long totalCount;
    private int totalPageCount;
    private int currentPage;
    private int pageSize;
    private List<UserDTO> users;
}
