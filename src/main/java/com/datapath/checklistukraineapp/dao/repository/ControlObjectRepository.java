package com.datapath.checklistukraineapp.dao.repository;

import com.datapath.checklistukraineapp.dao.entity.ControlObjectEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface ControlObjectRepository extends Neo4jRepository<ControlObjectEntity, String> {
}
