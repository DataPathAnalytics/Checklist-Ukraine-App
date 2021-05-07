package com.datapath.checklistapp.dao.repository;

import com.datapath.checklistapp.dao.entity.QuestionExecutionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionExecutionRepository extends JpaRepository<QuestionExecutionEntity, Long> {
}
