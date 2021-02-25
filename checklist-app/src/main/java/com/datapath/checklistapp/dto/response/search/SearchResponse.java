package com.datapath.checklistapp.dto.response.search;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SearchResponse<T> {
    private Integer currentPage;
    private Long totalItems;
    private Integer totalPages;
    private List<T> items;
}
