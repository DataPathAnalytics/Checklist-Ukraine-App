package com.datapath.checklistukraineapp.dao.repository;

import com.datapath.checklistukraineapp.dao.entity.AnswerStructureEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface AnswerStructureRepository extends Neo4jRepository<AnswerStructureEntity, Long> {
}
