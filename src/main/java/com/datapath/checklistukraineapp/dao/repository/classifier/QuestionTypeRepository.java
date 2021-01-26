package com.datapath.checklistukraineapp.dao.repository.classifier;

import com.datapath.checklistukraineapp.dao.entity.classifier.QuestionType;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface QuestionTypeRepository extends Neo4jRepository<QuestionType, Integer> {
}
