package com.datapath.checklistukraineapp.dao.repository;

import com.datapath.checklistukraineapp.dao.entity.classifier.PermissionClassifier;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.Optional;

public interface PermissionRepository extends Neo4jRepository<PermissionClassifier, Integer> {

    Optional<PermissionClassifier> findByRole(String role);
}
