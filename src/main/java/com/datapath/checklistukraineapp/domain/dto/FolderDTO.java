package com.datapath.checklistukraineapp.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FolderDTO {

    private Long id;
    private String name;
    private LocalDate dateCreated;
    private LocalDate dateModified;
    private Long parentId;
    private List<Long> children = new ArrayList<>();
}
