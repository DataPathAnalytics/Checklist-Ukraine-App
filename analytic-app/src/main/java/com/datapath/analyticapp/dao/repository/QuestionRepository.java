package com.datapath.analyticapp.dao.repository;


import com.datapath.analyticapp.dao.entity.imported.QuestionEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.Optional;

public interface QuestionRepository extends Neo4jRepository<QuestionEntity, Long> {

    Optional<QuestionEntity> findFirstByDateCreatedNotNullOrderByDateCreatedDesc();

}
