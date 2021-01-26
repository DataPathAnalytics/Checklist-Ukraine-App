package com.datapath.checklistukraineapp.dao.repository.classifier;

import com.datapath.checklistukraineapp.dao.entity.classifier.Authority;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface AuthorityRepository extends Neo4jRepository<Authority, Integer> {
}
