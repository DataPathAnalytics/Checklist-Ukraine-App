package com.datapath.checklistapp.dao.repository.classifier;

import com.datapath.checklistapp.dao.entity.classifier.ActivityStatus;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface ActivityStatusRepository extends Neo4jRepository<ActivityStatus, Integer> {
}
