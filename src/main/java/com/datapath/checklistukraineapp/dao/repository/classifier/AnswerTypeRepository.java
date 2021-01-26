package com.datapath.checklistukraineapp.dao.repository.classifier;

import com.datapath.checklistukraineapp.dao.entity.classifier.AnswerType;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface AnswerTypeRepository extends Neo4jRepository<AnswerType, Integer> {
}
