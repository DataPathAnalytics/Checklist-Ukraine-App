package com.datapath.checklistapp.dao.repository;

import com.datapath.checklistapp.dao.entity.InterpretationEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface InterpretationRepository extends Neo4jRepository<InterpretationEntity, Long> {
}
