package com.datapath.avtodormigration.dto.request;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ResponseSessionRequest {
    private Integer controlActivityId;
    private Integer templateId;
    private List<AnswerDTO> answers = new ArrayList<>();
    private boolean autoCreated = true;
}
