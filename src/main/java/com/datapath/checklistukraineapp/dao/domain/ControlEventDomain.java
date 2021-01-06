package com.datapath.checklistukraineapp.dao.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ControlEventDomain {

    private Long id;
    private String name;
    private Integer controlTypeId;
    private LocalDate dateCreated;
    private LocalDate dateModified;
    private Integer controlStatusId;
    private String controlObjectId;
    private String controlObjectName;
    private Long authorId;
}
