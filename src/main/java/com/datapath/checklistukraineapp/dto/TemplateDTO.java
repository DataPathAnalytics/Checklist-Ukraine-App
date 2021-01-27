package com.datapath.checklistukraineapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemplateDTO {
    private Long id;
    private String name;
    private LocalDate dateCreated;
    private Long authorId;
    private Long folderId;
    private Long templateConfigId;
    private Integer templateType;
}
