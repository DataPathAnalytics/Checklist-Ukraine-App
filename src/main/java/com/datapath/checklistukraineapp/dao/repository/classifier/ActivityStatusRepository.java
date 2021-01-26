package com.datapath.checklistukraineapp.dao.repository.classifier;

import com.datapath.checklistukraineapp.dao.entity.classifier.ActivityStatus;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface ActivityStatusRepository extends Neo4jRepository<ActivityStatus, Integer> {
}
