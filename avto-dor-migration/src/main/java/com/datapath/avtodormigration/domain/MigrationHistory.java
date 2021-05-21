package com.datapath.avtodormigration.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class MigrationHistory {
    @JsonProperty("contract_response_sessions")
    private List<MigrationState> contractResponseSessions;
}
