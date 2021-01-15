package com.datapath.checklistukraineapp.dao.repository;

import com.datapath.checklistukraineapp.dao.entity.QuestionEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface QuestionRepository extends Neo4jRepository<QuestionEntity, Long> {
}
