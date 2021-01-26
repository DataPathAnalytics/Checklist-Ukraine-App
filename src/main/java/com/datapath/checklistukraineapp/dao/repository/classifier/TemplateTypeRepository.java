package com.datapath.checklistukraineapp.dao.repository.classifier;

import com.datapath.checklistukraineapp.dao.entity.classifier.TemplateType;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface TemplateTypeRepository extends Neo4jRepository<TemplateType, Integer> {
}
