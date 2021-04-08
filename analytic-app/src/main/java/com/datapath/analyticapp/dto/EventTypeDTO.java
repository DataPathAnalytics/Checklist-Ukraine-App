package com.datapath.analyticapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EventTypeDTO {

    private Long id;
    private String eventTypeName;
}
