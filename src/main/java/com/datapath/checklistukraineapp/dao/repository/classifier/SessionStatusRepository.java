package com.datapath.checklistukraineapp.dao.repository.classifier;

import com.datapath.checklistukraineapp.dao.entity.classifier.SessionStatus;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface SessionStatusRepository extends Neo4jRepository<SessionStatus, Integer> {
}
