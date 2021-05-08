package com.datapath.checklistapp.dao.repository;

import com.datapath.checklistapp.dao.entity.QuestionSourceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionSourceRepository extends JpaRepository<QuestionSourceEntity, Integer> {

    QuestionSourceEntity findByIdentifier(Integer identifier);
}
