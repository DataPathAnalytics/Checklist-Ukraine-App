package com.datapath.checklistapp.dao.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ControlActivityDomain {

    private Long id;
    private Integer statusId;

    private List<Long> templateIds = new ArrayList<>();
    private Long activityResponseId;
    private List<Long> sessionResponseIds = new ArrayList<>();
}
