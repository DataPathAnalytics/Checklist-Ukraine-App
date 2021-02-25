package com.datapath.checklistapp.dao.repository.classifier;

import com.datapath.checklistapp.dao.entity.classifier.TemplateConfigType;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface TemplateConfigTypeRepository extends Neo4jRepository<TemplateConfigType, Integer> {
}
