package com.datapath.checklistapp.dao.repository;

import com.datapath.checklistapp.dao.entity.TemplateEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface TemplateRepository extends Neo4jRepository<TemplateEntity, Long> {
}
