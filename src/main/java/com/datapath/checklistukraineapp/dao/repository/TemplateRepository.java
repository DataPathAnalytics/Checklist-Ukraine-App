package com.datapath.checklistukraineapp.dao.repository;

import com.datapath.checklistukraineapp.dao.domain.TemplateDomain;
import com.datapath.checklistukraineapp.dao.entity.TemplateEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Set;

public interface TemplateRepository extends Neo4jRepository<TemplateEntity, Long> {

    @Query(value = "match (t:Template)-->(u:User), (t:Template)-->(f:TemplateFolder) return t, id(u) as authorId, id(f) as folderId")
    List<TemplateDomain> findTemplates();

    @Query(value = "match (t:Template) where id(t) in $ids return t")
    Set<TemplateEntity> findAllByIdIn(List<Long> ids);
}
