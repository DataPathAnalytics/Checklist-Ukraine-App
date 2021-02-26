package com.datapath.checklistapp.dao.repository;

import com.datapath.checklistapp.dao.entity.QuestionGroupEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface QuestionGroupRepository extends Neo4jRepository<QuestionGroupEntity, Long> {
}
