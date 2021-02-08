package com.datapath.checklistukraineapp.dao.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ControlActivityDomain {

    private Long id;
    private String name;
    private LocalDate dateCreated;
    private LocalDate dateModified;

    private Integer statusId;
    private Long authorId;
    private List<Long> memberIds = new ArrayList<>();
    private List<Long> templateIds = new ArrayList<>();
    private Long activityResponseId;
    private List<Long> sessionResponseIds = new ArrayList<>();
}
