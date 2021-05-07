package com.datapath.checklistapp.dao.repository;

import com.datapath.checklistapp.dao.entity.AnswerStructureEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerStructureRepository extends JpaRepository<AnswerStructureEntity, Long> {

    Page<AnswerStructureEntity> findByNameMatchesRegexOrderByName(String name, Pageable pageable);

    AnswerStructureEntity findFirstBy_defaultIsTrue();
}
