package com.datapath.checklistukraineapp.dao.repository;

import com.datapath.checklistukraineapp.dao.entity.classifier.FinancingTypeClassifier;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface FinancingTypeRepository extends Neo4jRepository<FinancingTypeClassifier, Integer> {
}
