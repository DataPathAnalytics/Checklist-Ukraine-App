package com.datapath.checklistukraineapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ControlActivityDTO {

    private Long id;
    private String name;
    private LocalDate dateCreated;
    private LocalDate dateModified;
    private Integer statusId;
    private Long authorId;
    private List<TemplateDTO> templates;
    private Set<Long> members;

    private QuestionExecutionDTO objectQuestion;
    private List<QuestionExecutionDTO> objectFeatureQuestions;
    private List<QuestionExecutionDTO> typeQuestions;
    private List<QuestionExecutionDTO> authorityQuestions;

    private SessionPageDTO sessions;
}
