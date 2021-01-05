package com.datapath.checklistukraineapp.dto;

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
    private boolean root;
    private List<Long> children = new ArrayList<>();
}
