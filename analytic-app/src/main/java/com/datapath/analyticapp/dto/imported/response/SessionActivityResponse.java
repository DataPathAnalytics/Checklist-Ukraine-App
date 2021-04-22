package com.datapath.analyticapp.dto.imported.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SessionActivityResponse {
    private List<ResponseDataDTO> data;
    private LocalDateTime nextOffset;
}
