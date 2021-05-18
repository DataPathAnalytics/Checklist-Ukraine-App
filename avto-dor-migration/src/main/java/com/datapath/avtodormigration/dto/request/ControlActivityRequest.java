package com.datapath.avtodormigration.dto.request;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ControlActivityRequest {
    private Integer templateConfigId;
    private List<Integer> templateIds;
    private List<AnswerDTO> answers = new ArrayList<>();
}
