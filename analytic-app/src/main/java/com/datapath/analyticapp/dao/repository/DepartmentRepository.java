package com.datapath.analyticapp.dao.repository;

import com.datapath.analyticapp.dao.entity.imported.DepartmentEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.Optional;

public interface DepartmentRepository extends Neo4jRepository<DepartmentEntity, Long> {
    Optional<DepartmentEntity> findFirstByOuterId(Long outerId);
}
