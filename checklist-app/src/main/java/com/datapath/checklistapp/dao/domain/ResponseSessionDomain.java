package com.datapath.checklistapp.dao.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseSessionDomain {
    private Long id;
    private String name;
    private LocalDateTime dateCreated;
    private LocalDateTime dateModified;

    private Integer sessionStatusId;
    private Long templateId;
    private String templateName;
    private Long reviewerId;
    private Long authorId;
}
