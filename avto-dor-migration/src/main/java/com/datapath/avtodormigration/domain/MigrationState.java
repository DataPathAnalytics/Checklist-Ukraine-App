package com.datapath.avtodormigration.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MigrationState {
    private String identifier;
    private Integer id;
}
