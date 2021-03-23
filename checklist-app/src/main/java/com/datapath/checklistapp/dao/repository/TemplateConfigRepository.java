package com.datapath.checklistapp.dao.repository;

import com.datapath.checklistapp.dao.entity.TemplateConfigEntity;
import com.datapath.checklistapp.dao.entity.classifier.TemplateConfigType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;

public interface TemplateConfigRepository extends Neo4jRepository<TemplateConfigEntity, Long> {

    String IS_USED_QUERY = "match (t:TemplateConfig)<-[r]-(n) where id(t)=$id return count(r) > 0";

    List<TemplateConfigEntity> findAllByType(TemplateConfigType type);

    Page<TemplateConfigEntity> findByNameMatchesRegexOrderByName(String name, Pageable pageable);

    @Query(value = IS_USED_QUERY)
    boolean isUsed(Long id);
}
