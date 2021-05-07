package com.datapath.analyticapp.dto.imported.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SessionDTO {

    private Long id;
    private LocalDateTime dateCreated;
    private LocalDateTime dateModified;
    private Long authorId;
    private Long reviewerId;
    private List<Long> members;
    private TemplateDTO template;
    private List<AnswerDTO> answers;
    private boolean invalid;
}
