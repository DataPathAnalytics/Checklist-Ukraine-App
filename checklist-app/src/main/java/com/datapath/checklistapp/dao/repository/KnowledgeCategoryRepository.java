package com.datapath.checklistapp.dao.repository;

import com.datapath.checklistapp.dao.entity.KnowledgeCategoryEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

public interface KnowledgeCategoryRepository extends Neo4jRepository<KnowledgeCategoryEntity, Long> {
    List<KnowledgeCategoryEntity> findTop20ByNameMatchesRegexOrderByName(String name);
}
