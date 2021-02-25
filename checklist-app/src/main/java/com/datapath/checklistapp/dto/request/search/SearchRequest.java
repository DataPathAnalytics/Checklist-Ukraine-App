package com.datapath.checklistapp.dto.request.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequest {

    private String keyword = "";
    private int page = 0;
    private int size = 20;
}
