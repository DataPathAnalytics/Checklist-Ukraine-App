package com.datapath.checklistapp.dao.repository;

import com.datapath.checklistapp.dao.entity.TemplateFolderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface TemplateFolderRepository extends Neo4jRepository<TemplateFolderEntity, Long> {
    Page<TemplateFolderEntity> findByNameMatchesRegexOrderByName(String name, Pageable pageable);
}
