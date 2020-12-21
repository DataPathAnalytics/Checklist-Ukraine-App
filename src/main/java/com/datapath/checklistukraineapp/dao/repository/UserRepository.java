package com.datapath.checklistukraineapp.dao.repository;

import com.datapath.checklistukraineapp.dao.entity.UserEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface UserRepository extends Neo4jRepository<UserEntity, Long> {

    UserEntity findByEmail(String email);

    UserEntity findByEmailAndRemovedIsFalse(String email);

    UserEntity findFirstByRole(String role);
}
