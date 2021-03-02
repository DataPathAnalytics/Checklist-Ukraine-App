package com.datapath.checklistapp.dto.response.page;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PageableResponse<T> {
    private Integer currentPage;
    private Long totalItems;
    private Integer totalPages;
    private List<T> items;
}
