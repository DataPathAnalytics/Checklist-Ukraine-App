package com.datapath.checklistapp.dao.repository;

import com.datapath.checklistapp.dao.entity.QuestionSourceEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface QuestionSourceRepository extends Neo4jRepository<QuestionSourceEntity, Long> {

    QuestionSourceEntity findByIdentifier(Long identifier);
}
