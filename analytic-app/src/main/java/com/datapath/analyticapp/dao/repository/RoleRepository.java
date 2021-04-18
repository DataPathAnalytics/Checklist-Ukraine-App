package com.datapath.analyticapp.dao.repository;

import com.datapath.analyticapp.dao.entity.RoleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface RoleRepository extends Neo4jRepository<RoleEntity, Long> {
    Page<RoleEntity> findByRoleNameMatchesRegexOrderByRoleName(String name, Pageable pageable);
}
