package com.datapath.checklistukraineapp.dto;

import lombok.Data;

import java.util.List;

@Data
public class ChecklistPageDTO {
    private long totalCount;
    private int totalPageCount;
    private int currentPage;
    private int pageSize;
    private List<ChecklistDTO> checklists;
}
