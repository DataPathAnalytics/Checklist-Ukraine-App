package com.datapath.checklistapp.dao.repository;

import com.datapath.checklistapp.dao.entity.TemplateConfigEntity;
import com.datapath.checklistapp.dao.entity.classifier.TemplateType;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

public interface TemplateConfigRepository extends Neo4jRepository<TemplateConfigEntity, Long> {

    List<TemplateConfigEntity> findAllByType(TemplateType type);

    List<TemplateConfigEntity> findTop20ByNameStartingWithOrderByName(String name);
}
