package com.datapath.checklistukraineapp.dto.request.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateControlEventRequest {

    @NotBlank
    private String name;
    @NotNull
    private Integer controlTypeId;
    @NotNull
    private String controlObjectId;
    @NotNull
    private Long teamLeadId;

    private Set<Long> memberIds = new HashSet<>();

    private Set<Long> templateIds;
}
