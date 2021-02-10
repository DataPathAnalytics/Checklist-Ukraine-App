package com.datapath.checklistapp.dao.repository.classifier;

import com.datapath.checklistapp.dao.entity.classifier.SessionStatus;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface SessionStatusRepository extends Neo4jRepository<SessionStatus, Integer> {
}
