package com.datapath.checklistukraineapp.dao.repository;

import com.datapath.checklistukraineapp.dao.entity.DepartmentEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface DepartmentRepository extends Neo4jRepository<DepartmentEntity, String> {
}
