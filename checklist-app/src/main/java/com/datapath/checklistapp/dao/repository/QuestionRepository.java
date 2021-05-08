package com.datapath.checklistapp.dao.repository;

import com.datapath.checklistapp.dao.entity.QuestionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionRepository extends JpaRepository<QuestionEntity, Integer> {

    String IDENTIFIER_QUESTION_IDS_QUERY = "select q.* from question q " +
            "join answer_structure a on a.id = q.answer_structure_id " +
            "join field_description f on a.id = f.answer_structure_id " +
            "where f.identifier is true and " +
            "q.value ilike CONCAT(:value, '%') order by q.value";

    @Query(value = "select * from question where value ilike CONCAT(:value, '%') order by value", nativeQuery = true)
    Page<QuestionEntity> searchByValue(@Param("value") String value, Pageable pageable);

    @Query(value = IDENTIFIER_QUESTION_IDS_QUERY, nativeQuery = true)
    Page<QuestionEntity> findByValueWithIdentifier(@Param("value") String value, Pageable pageable);
}
