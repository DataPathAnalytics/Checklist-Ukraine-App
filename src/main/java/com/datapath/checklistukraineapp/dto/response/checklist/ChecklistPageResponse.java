package com.datapath.checklistukraineapp.dto.response.checklist;

import com.datapath.checklistukraineapp.dto.ChecklistDTO;
import lombok.Data;

import java.util.List;

@Data
public class ChecklistPageResponse {
    private long totalCount;
    private int totalPageCount;
    private int currentPage;
    private int pageSize;
    private List<ChecklistDTO> checklists;
}
