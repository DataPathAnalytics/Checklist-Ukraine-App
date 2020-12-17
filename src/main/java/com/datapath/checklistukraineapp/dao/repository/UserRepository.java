package com.datapath.checklistukraineapp.dao.repository;

import com.datapath.checklistukraineapp.dao.node.UserNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface UserRepository extends Neo4jRepository<UserNode, Long> {

    UserNode findByEmail(String email);
}
