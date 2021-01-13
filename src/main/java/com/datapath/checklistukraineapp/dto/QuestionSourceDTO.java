package com.datapath.checklistukraineapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionSourceDTO {
    private String id;
    private String name;
    private String link;
}