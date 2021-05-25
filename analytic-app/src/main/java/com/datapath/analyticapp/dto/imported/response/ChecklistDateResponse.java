package com.datapath.analyticapp.dto.imported.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ChecklistDateResponse {
    private List<ChecklistDateDTO> data = new ArrayList<>();
    private LocalDateTime nextOffset;
}
