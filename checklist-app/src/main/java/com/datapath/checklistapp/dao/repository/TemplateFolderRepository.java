package com.datapath.checklistapp.dao.repository;

import com.datapath.checklistapp.dao.entity.TemplateFolderEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

public interface TemplateFolderRepository extends Neo4jRepository<TemplateFolderEntity, Long> {
    List<TemplateFolderEntity> findTop20ByNameMatchesRegexOrderByName(String name);
}
