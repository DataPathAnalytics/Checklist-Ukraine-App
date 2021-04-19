package com.datapath.analyticapp.dao.repository;

import com.datapath.analyticapp.dao.entity.imported.AnswerStructureEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface AnswerStructureRepository extends Neo4jRepository<AnswerStructureEntity, Long> {

    AnswerStructureEntity findFirstByOuterId(Long outerId);
}
