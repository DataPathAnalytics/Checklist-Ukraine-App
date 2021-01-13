package com.datapath.checklistukraineapp.dao.repository;

import com.datapath.checklistukraineapp.dao.entity.classifier.ControlTypeEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface ControlTypeRepository extends Neo4jRepository<ControlTypeEntity, Integer> {
}