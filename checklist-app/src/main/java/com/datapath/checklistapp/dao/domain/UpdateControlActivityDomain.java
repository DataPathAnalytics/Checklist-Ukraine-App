package com.datapath.checklistapp.dao.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateControlActivityDomain {

    private Long id;
    private String name;
    private LocalDateTime dateCreated;
    private Long authorId;
    private List<Long> memberIds;
    private List<Long> sessionsIds;
}
