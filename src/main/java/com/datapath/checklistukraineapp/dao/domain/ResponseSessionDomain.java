package com.datapath.checklistukraineapp.dao.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseSessionDomain {

    private Long id;
    private Integer sessionStatusId;
    private String name;
    private LocalDate dateCreated;
    private LocalDate dateModified;
    private Long templateId;
    private String templateName;
    private Long reviewerId;
    private Long authorId;
}
