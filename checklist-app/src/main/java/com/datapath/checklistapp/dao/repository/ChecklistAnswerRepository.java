package com.datapath.checklistapp.dao.repository;

import com.datapath.checklistapp.dao.entity.AnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChecklistAnswerRepository extends JpaRepository<AnswerEntity, Integer> {
}
