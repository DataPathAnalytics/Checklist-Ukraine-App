package com.datapath.checklistukraineapp.dao.repository;

import com.datapath.checklistukraineapp.dao.entity.PermissionEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.Optional;

public interface PermissionRepository extends Neo4jRepository<PermissionEntity, Long> {

    Optional<PermissionEntity> findByRole(String role);
}
