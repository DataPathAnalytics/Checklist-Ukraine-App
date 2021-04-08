package com.datapath.analyticapp.dao.repository;

import com.datapath.analyticapp.dao.entity.KnowledgeClassEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface KnowledgeClassRepository extends Neo4jRepository<KnowledgeClassEntity, Long> {
    Page<KnowledgeClassEntity> findByKnowledgeClassNameMatchesRegexOrderByKnowledgeClassName(String name, Pageable pageable);
}
