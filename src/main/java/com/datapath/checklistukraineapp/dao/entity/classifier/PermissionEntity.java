package com.datapath.checklistukraineapp.dao.entity.classifier;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Version;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Data
@Node("Permission")
@NoArgsConstructor
@AllArgsConstructor
public class PermissionEntity {

    @Id
    private Integer permissionId;
    private String role;
    private String value;
    @Version
    private Long version;
}
