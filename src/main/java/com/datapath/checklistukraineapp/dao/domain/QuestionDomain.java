package com.datapath.checklistukraineapp.dao.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class QuestionDomain {

    private Long id;
    private String name;
    private LocalDate dateCreated;
    private LocalDate dateModified;

    private String questionSourceId;
    private String questionSourceName;
    private String questionSourceLink;
    private String documentParagraph;

    private String groupName;
    private Long parentQuestionId;
    private Integer parentAnswerId;

}
