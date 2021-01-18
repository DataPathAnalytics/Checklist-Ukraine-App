package com.datapath.checklistukraineapp.dao.repository;

import com.datapath.checklistukraineapp.dao.entity.classifier.AnswerClassifier;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface AnswerRepository extends Neo4jRepository<AnswerClassifier, Integer> {
}
