package com.datapath.checklistapp.dao.repository;

import com.datapath.checklistapp.dao.entity.AnswerStructureEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface AnswerStructureRepository extends Neo4jRepository<AnswerStructureEntity, Long> {

    Page<AnswerStructureEntity> findByNameMatchesRegexOrderByName(String name, Pageable pageable);

    AnswerStructureEntity findFirstByName(String name);
}
