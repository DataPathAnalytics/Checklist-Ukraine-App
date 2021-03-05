package com.datapath.analyticapp.dao.repository;

import com.datapath.analyticapp.dao.entity.InterpretationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface InterpretationRepository extends Neo4jRepository<InterpretationEntity, Long> {
    Page<InterpretationEntity> findByInterpretationMatchesRegexOrderByInterpretation(String regex, Pageable pageable);
}
