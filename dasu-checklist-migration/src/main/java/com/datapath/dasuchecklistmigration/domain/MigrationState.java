package com.datapath.dasuchecklistmigration.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MigrationState {
    @JsonProperty("checklist_id")
    private Integer checklistId;
    @JsonProperty("response_session_id")
    private Integer responseSessionId;
    @JsonProperty("control_activity_id")
    private Integer controlActivityId;
}
