package com.datapath.checklistapp.dao.repository;

import com.datapath.checklistapp.dao.entity.KnowledgeClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KnowledgeClassRepository extends JpaRepository<KnowledgeClassEntity, Long> {
    Optional<KnowledgeClassEntity> findByOuterId(Long outerId);
}
