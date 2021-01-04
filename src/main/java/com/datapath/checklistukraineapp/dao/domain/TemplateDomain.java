package com.datapath.checklistukraineapp.dao.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class TemplateDomain {

    private Long id;
    private String name;
    private LocalDate dateCreated;
    private Long authorId;
    private Long folderId;
}
