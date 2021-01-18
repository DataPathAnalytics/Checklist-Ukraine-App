package com.datapath.checklistukraineapp.dto.response.checklist;

import com.datapath.checklistukraineapp.dto.QuestionAnswerDTO;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ChecklistResponse {

    private Long id;
    private String name;
    private LocalDate dateCreated;
    private LocalDate dateModified;
    private LocalDate checkingPeriodStart;
    private LocalDate checkingPeriodEnd;
    private Double amount;
    private Double spentAmount;
    private Double controlAmount;
    private Double violationAmount;
    private String summary;
    private Integer checklistStatusId;
    private Long templateId;
    private Long reviewerId;
    private Long authorId;

    private List<QuestionAnswerDTO> questionAnswerList;
}
