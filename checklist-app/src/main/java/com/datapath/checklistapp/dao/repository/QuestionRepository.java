package com.datapath.checklistapp.dao.repository;

import com.datapath.checklistapp.dao.entity.QuestionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface QuestionRepository extends Neo4jRepository<QuestionEntity, Long> {

    String IDENTIFIER_QUESTION_IDS_QUERY = "match (q:Question)-->(as:AnswerStructure)-->(f:FieldDescription) " +
            "where f.identifier=true and q.value =~ $value " +
            "with id(q) as id order by q.value skip $skip limit $limit " +
            "return id";

    String IDENTIFIER_QUESTION_COUNT_QUERY = "match (q:Question)-->(as:AnswerStructure)-->(f:FieldDescription) " +
            "where f.identifier=true and q.value =~ $value " +
            "return count(q)";

    Page<QuestionEntity> findByValueMatchesRegexOrderByValue(String value, Pageable pageable);

    List<QuestionEntity> findAllByDateCreatedAfterOrderByDateCreated(LocalDateTime date, Pageable pageable);

    @Query(value = IDENTIFIER_QUESTION_IDS_QUERY)
    List<Long> findWithIdentifier(String value, int skip, int limit);

    @Query(value = IDENTIFIER_QUESTION_COUNT_QUERY)
    Long countIdentifier(String value);
}
