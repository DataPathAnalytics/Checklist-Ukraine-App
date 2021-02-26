package com.datapath.checklistapp.dao.repository;

import com.datapath.checklistapp.dao.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;

public interface UserRepository extends Neo4jRepository<UserEntity, Long> {

    UserEntity findByEmail(String email);

    UserEntity findByEmailAndRemovedIsFalse(String email);

    @Query(value = "match (u:User)-[hp:HAS_PERMISSION]->(p:Permission {role:'admin'}) return u")
    List<UserEntity> findAdmins();

    @Query(value = "match (u:User) where u.locked = true return count(u) > 0")
    boolean existsNotChecked();

    Page<UserEntity> findAllByRemovedIsFalseAndSuperAdminIsFalseOrderByLockedDescRegisteredDateDescFirstNameAsc(Pageable pageable);
}
