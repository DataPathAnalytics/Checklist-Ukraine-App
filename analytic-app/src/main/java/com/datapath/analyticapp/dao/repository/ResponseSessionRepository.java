package com.datapath.analyticapp.dao.repository;

import com.datapath.analyticapp.dao.entity.imported.ResponseSessionEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.Optional;

public interface ResponseSessionRepository extends Neo4jRepository<ResponseSessionEntity, Long> {
    Optional<ResponseSessionEntity> findFirstByDateModifiedNotNullOrderByDateModifiedDesc();

    ResponseSessionEntity findByOuterId(Long outerId);
}
