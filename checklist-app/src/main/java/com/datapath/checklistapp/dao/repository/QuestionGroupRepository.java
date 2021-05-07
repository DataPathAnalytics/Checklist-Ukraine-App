package com.datapath.checklistapp.dao.repository;

import com.datapath.checklistapp.dao.entity.QuestionGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionGroupRepository extends JpaRepository<QuestionGroupEntity, Long> {
}
