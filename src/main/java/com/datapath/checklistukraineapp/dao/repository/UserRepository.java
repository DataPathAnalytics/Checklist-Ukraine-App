package com.datapath.checklistukraineapp.dao.repository;

import com.datapath.checklistukraineapp.dao.node.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface UserRepository extends Neo4jRepository<User, Long> {

    User findByEmail(String email);

    User findByEmailAndRemovedIsFalse(String email);
}
