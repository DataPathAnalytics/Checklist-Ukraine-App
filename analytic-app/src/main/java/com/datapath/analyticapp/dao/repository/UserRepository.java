package com.datapath.analyticapp.dao.repository;

import com.datapath.analyticapp.dao.entity.imported.UserEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.Optional;

public interface UserRepository extends Neo4jRepository<UserEntity, Long> {

    Optional<UserEntity> findFirstByDateModifiedNotNullOrderByDateModifiedDesc();

    Optional<UserEntity> findFirstByEmail(String email);
}
