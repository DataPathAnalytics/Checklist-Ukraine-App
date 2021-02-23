package com.datapath.checklistapp.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class QuestionDTO {

    private Long id;
    private String name;
    private LocalDateTime dateCreated;

    private List<IdValueDTO> knowledgeCategories;

    private String questionSourceId;
    private String questionSourceName;
    private String questionSourceLink;
    private String documentParagraph;

    private AnswerStructureDTO answerStructure;
    private Integer questionTypeId;
    private String questionTypeValue;
}
