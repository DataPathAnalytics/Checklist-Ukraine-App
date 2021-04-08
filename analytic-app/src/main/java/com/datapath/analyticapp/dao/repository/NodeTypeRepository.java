package com.datapath.analyticapp.dao.repository;

import com.datapath.analyticapp.dao.entity.NodeTypeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface NodeTypeRepository extends Neo4jRepository<NodeTypeEntity, Long> {
    Page<NodeTypeEntity> findByNodeTypeNameMatchesRegexOrderByNodeTypeName(String name, Pageable pageable);
}
