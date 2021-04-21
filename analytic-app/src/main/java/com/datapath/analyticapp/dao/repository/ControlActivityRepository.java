package com.datapath.analyticapp.dao.repository;

import com.datapath.analyticapp.dao.entity.imported.ControlActivityEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.Optional;

public interface ControlActivityRepository extends Neo4jRepository<ControlActivityEntity, Long> {
    Optional<ControlActivityEntity> findFirstByDateModifiedNotNullOrderByDateModifiedDesc();
}
