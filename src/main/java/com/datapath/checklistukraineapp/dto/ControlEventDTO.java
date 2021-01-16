package com.datapath.checklistukraineapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ControlEventDTO {

    private Long id;
    private String name;
    private Integer controlTypeId;
    private LocalDate dateCreated;
    private LocalDate dateModified;
    private Integer controlStatusId;
    private String controlObjectId;
    private String controlObjectName;
    private Long authorId;

    private List<TemplateDTO> templates;
    private Set<Long> members;

    private ChecklistPageDTO checklists;
}
