package com.datapath.analyticapp.dto.imported.response;

import lombok.Data;

import java.util.List;

@Data
public class ResponseDataDTO {
    private Long id;
    private SessionDTO activity;
    private List<SessionDTO> sessions;
}
