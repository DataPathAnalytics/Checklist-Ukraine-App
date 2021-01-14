package com.datapath.checklistukraineapp.dao.repository;

import com.datapath.checklistukraineapp.dao.entity.ChecklistAnswerEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface ChecklistAnswerRepository extends Neo4jRepository<ChecklistAnswerEntity, Long> {
}
