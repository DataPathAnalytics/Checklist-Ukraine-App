package com.datapath.checklistapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ControlActivityDTO {

    private Integer id;
    private Integer statusId;
    private ResponseSessionDTO activity;
    private List<TemplateDTO> templates;
}
