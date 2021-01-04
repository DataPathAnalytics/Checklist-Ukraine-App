package com.datapath.checklistukraineapp.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class QuestionDTO {

    private Long id;
    private String name;
    private LocalDate dateModified;
    private LocalDate dateCreated;
    private String questionSourceId;
    private String documentParagraph;
}
