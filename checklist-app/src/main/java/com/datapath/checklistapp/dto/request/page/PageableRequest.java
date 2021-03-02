package com.datapath.checklistapp.dto.request.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageableRequest {
    private int page = 0;
    private int size = 20;
}
