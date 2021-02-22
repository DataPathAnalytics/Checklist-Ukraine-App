package com.datapath.checklistapp.dao.repository;

import com.datapath.checklistapp.dao.entity.TemplateConfigFolderEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

public interface TemplateConfigFolderRepository extends Neo4jRepository<TemplateConfigFolderEntity, Long> {
    List<TemplateConfigFolderEntity> findTop20ByNameMatchesRegexOrderByName(String name);
}
