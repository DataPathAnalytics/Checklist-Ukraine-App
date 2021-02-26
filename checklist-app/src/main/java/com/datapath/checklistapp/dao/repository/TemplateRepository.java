package com.datapath.checklistapp.dao.repository;

import com.datapath.checklistapp.dao.entity.TemplateEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

public interface TemplateRepository extends Neo4jRepository<TemplateEntity, Long> {

    Page<TemplateEntity> findByNameMatchesRegexOrderByName(String name, Pageable pageable);

    @Query(value = "match (t:Template)<-[r]-(n) where id(t)=$id return count(r) > 0")
    boolean isUsed(Long id);
}
