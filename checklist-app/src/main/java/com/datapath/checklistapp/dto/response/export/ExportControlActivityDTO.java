package com.datapath.checklistapp.dto.response.export;

import com.datapath.checklistapp.dto.ControlActivityDTO;
import com.datapath.checklistapp.dto.ResponseSessionDTO;
import lombok.Data;

import java.util.List;

@Data
public class ExportControlActivityDTO {

    private ControlActivityDTO activity;
    private List<ResponseSessionDTO> sessions;
}
