package com.datapath.analyticapp.dto.imported.response;

import lombok.Data;

import java.util.List;

@Data
public class AnswerStructureDTO {
    private List<FieldDescriptionDTO> fieldDescriptions;
}