package com.datapath.analyticapp.dto.imported.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class SessionDTO {

    private Long id;
    private LocalDateTime dateCreated;
    private LocalDateTime dateModified;
    private Long authorId;
    private Long reviewerId;
    private List<Long> members = new ArrayList<>();
    private TemplateDTO template;
    private List<AnswerDTO> answers = new ArrayList<>();
    private boolean invalid;
}
