package com.datapath.checklistukraineapp.dto;

import lombok.Data;

import java.util.List;

@Data
public class SessionPageDTO {

    private long totalCount;
    private int totalPageCount;
    private int currentPage;
    private int pageSize;
    private List<ResponseSessionDTO> sessions;
}
