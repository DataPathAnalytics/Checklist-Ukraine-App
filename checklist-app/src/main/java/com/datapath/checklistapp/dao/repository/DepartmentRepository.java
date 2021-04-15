package com.datapath.checklistapp.dao.repository;

import com.datapath.checklistapp.dao.entity.DepartmentEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface DepartmentRepository extends Neo4jRepository<DepartmentEntity, Long> {

    DepartmentEntity findByIdentifier(Long identifier);
}
