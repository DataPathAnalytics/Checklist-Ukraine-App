package com.datapath.checklistukraineapp.dao.repository;

import com.datapath.checklistukraineapp.dao.entity.KnowledgeCategoryEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface KnowledgeCategoryRepository extends Neo4jRepository<KnowledgeCategoryEntity, Long> {
}
