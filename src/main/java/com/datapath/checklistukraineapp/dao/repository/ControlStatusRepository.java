package com.datapath.checklistukraineapp.dao.repository;

import com.datapath.checklistukraineapp.dao.entity.classifier.ControlStatusEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface ControlStatusRepository extends Neo4jRepository<ControlStatusEntity, Integer> {
}
