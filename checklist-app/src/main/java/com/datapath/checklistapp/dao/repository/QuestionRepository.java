package com.datapath.checklistapp.dao.repository;

import com.datapath.checklistapp.dao.entity.QuestionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface QuestionRepository extends Neo4jRepository<QuestionEntity, Long> {

    Page<QuestionEntity> findByNameMatchesRegexOrderByName(String name, Pageable pageable);

    List<QuestionEntity> findAllByDateCreatedAfterOrderByDateCreated(LocalDateTime date, Pageable pageable);

    @Query(value = "match (q:Question)-->(as:AnswerStructure)-->(f:FieldDescription) where f.identifier=true return id(q)")
    Page<Long> findWithIdentifier(String format, Pageable pageable);
}
