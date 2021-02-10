package com.datapath.checklistapp.dao.repository;

import com.datapath.checklistapp.dao.entity.KnowledgeCategoryEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface KnowledgeCategoryRepository extends Neo4jRepository<KnowledgeCategoryEntity, Long> {
}
