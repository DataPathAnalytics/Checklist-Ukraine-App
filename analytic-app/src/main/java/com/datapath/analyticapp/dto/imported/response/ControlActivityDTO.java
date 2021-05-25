package com.datapath.analyticapp.dto.imported.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ControlActivityDTO {
    private Long id;
    private SessionDTO activity;
    private List<SessionDTO> sessions = new ArrayList<>();
}
