package com.datapath.analyticapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class NodeTypeDTO {

    private Long id;
    private String nodeTypeName;

    private List<LinkTypeDTO> linkTypes;
}
