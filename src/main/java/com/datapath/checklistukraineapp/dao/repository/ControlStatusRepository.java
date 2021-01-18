package com.datapath.checklistukraineapp.dao.repository;

import com.datapath.checklistukraineapp.dao.entity.classifier.ControlStatusClassifier;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface ControlStatusRepository extends Neo4jRepository<ControlStatusClassifier, Integer> {
}
