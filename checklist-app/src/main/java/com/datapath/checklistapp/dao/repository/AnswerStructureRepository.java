package com.datapath.checklistapp.dao.repository;

import com.datapath.checklistapp.dao.entity.AnswerStructureEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

public interface AnswerStructureRepository extends Neo4jRepository<AnswerStructureEntity, Long> {

    List<AnswerStructureEntity> findTop20ByNameStartingWithOrderByName(String name);
}
