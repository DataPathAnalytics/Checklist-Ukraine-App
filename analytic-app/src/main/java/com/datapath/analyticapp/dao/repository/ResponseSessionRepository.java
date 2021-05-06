package com.datapath.analyticapp.dao.repository;

import com.datapath.analyticapp.dao.entity.imported.ResponseSessionEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface ResponseSessionRepository extends Neo4jRepository<ResponseSessionEntity, Long> {

    ResponseSessionEntity findByOuterId(Long outerId);
}
