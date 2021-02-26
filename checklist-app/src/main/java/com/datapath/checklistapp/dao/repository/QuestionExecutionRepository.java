package com.datapath.checklistapp.dao.repository;

import com.datapath.checklistapp.dao.entity.QuestionExecutionEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface QuestionExecutionRepository extends Neo4jRepository<QuestionExecutionEntity, Long> {
}
