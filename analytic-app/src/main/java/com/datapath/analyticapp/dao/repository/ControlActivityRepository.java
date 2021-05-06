package com.datapath.analyticapp.dao.repository;

import com.datapath.analyticapp.dao.entity.imported.ControlActivityEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface ControlActivityRepository extends Neo4jRepository<ControlActivityEntity, Long> {

    ControlActivityEntity findByOuterId(Long id);
}
