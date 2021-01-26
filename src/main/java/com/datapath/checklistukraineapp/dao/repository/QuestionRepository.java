package com.datapath.checklistukraineapp.dao.repository;

import com.datapath.checklistukraineapp.dao.entity.QuestionEntity;
import com.datapath.checklistukraineapp.dao.entity.classifier.QuestionType;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

public interface QuestionRepository extends Neo4jRepository<QuestionEntity, Long> {

    List<QuestionEntity> findAllByTypeIn(List<QuestionType> types);
}
