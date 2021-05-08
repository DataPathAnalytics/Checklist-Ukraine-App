package com.datapath.checklistapp.dao.repository;

import com.datapath.checklistapp.dao.entity.AnswerStructureEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AnswerStructureRepository extends JpaRepository<AnswerStructureEntity, Integer> {

    @Query(value = "select * from answer_structure where name ilike CONCAT(:name, '%') order by name", nativeQuery = true)
    Page<AnswerStructureEntity> searchByName(@Param("name") String name, Pageable pageable);

    AnswerStructureEntity findFirstBy_defaultIsTrue();
}
