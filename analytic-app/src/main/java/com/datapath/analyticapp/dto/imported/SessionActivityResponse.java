package com.datapath.analyticapp.dto.imported;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class SessionActivityResponse {
    private List<Map<String, Object>> data;
    private LocalDateTime nextOffset;
}
