package com.datapath.analyticapp.dao.repository;

import com.datapath.analyticapp.dao.entity.KnowledgeCategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface KnowledgeCategoryRepository extends Neo4jRepository<KnowledgeCategoryEntity, Long> {
    Page<KnowledgeCategoryEntity> findByNameMatchesRegexOrderByName(String name, Pageable pageable);
}
