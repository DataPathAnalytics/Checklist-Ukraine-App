package com.datapath.checklistapp.dao.repository.classifier;

import com.datapath.checklistapp.dao.entity.classifier.Permission;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.Optional;

public interface PermissionRepository extends Neo4jRepository<Permission, Integer> {

    Optional<Permission> findByRole(String role);
}
