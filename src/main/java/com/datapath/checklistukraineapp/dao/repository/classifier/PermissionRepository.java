package com.datapath.checklistukraineapp.dao.repository.classifier;

import com.datapath.checklistukraineapp.dao.entity.classifier.Permission;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.Optional;

public interface PermissionRepository extends Neo4jRepository<Permission, Integer> {

    Optional<Permission> findByRole(String role);
}
