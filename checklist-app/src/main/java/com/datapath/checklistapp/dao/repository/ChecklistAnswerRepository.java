package com.datapath.checklistapp.dao.repository;

import com.datapath.checklistapp.dao.entity.AnswerEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface ChecklistAnswerRepository extends Neo4jRepository<AnswerEntity, Long> {
}
