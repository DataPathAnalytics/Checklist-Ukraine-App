package com.datapath.checklistukraineapp.dao.repository;

import com.datapath.checklistukraineapp.dao.entity.classifier.FinancingDirectionClassifier;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface FinancingDirectionRepository extends Neo4jRepository<FinancingDirectionClassifier, Integer> {
}
