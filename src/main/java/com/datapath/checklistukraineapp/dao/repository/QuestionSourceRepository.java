package com.datapath.checklistukraineapp.dao.repository;

import com.datapath.checklistukraineapp.dao.entity.QuestionSourceEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface QuestionSourceRepository extends Neo4jRepository<QuestionSourceEntity, String> {
}
