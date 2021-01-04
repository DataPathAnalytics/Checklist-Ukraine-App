package com.datapath.checklistukraineapp.dao.repository;

import com.datapath.checklistukraineapp.dao.entity.TemplateFolderEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface TemplateFolderRepository extends Neo4jRepository<TemplateFolderEntity, Long> {
}
