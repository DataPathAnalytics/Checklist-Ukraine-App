package com.datapath.checklistukraineapp.dao.repository.classifier;

import com.datapath.checklistukraineapp.dao.entity.classifier.KnowledgeCategory;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface KnowledgeCategoryRepository extends Neo4jRepository<KnowledgeCategory, Integer> {
}
