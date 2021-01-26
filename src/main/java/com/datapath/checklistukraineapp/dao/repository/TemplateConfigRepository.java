package com.datapath.checklistukraineapp.dao.repository;

import com.datapath.checklistukraineapp.dao.entity.TemplateConfigEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface TemplateConfigRepository extends Neo4jRepository<TemplateConfigEntity, Long> {
}
