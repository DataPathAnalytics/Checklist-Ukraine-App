package com.datapath.checklistukraineapp.dao.repository;

import com.datapath.checklistukraineapp.dao.node.Department;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface DepartmentRepository extends Neo4jRepository<Department, Long> {
}
