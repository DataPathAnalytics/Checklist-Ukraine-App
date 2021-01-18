package com.datapath.checklistukraineapp.dao.repository;

import com.datapath.checklistukraineapp.dao.entity.classifier.ChecklistStatusClassifier;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface ChecklistStatusRepository extends Neo4jRepository<ChecklistStatusClassifier, Integer> {
}
