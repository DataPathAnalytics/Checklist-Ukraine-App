package com.datapath.analyticapp.dao.repository;

import com.datapath.analyticapp.dao.entity.EventTypeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface EventTypeRepository extends Neo4jRepository<EventTypeEntity, Long> {
    Page<EventTypeEntity> findByEventTypeNameMatchesRegexOrderByEventTypeName(String name, Pageable pageable);
}
