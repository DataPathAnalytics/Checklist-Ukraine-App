package com.datapath.checklistapp.dao.repository;

import com.datapath.checklistapp.dao.entity.KnowledgeClassEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.Optional;

public interface KnowledgeClassRepository extends Neo4jRepository<KnowledgeClassEntity, Long> {
    Optional<KnowledgeClassEntity> findByOuterId(Long outerId);
}
