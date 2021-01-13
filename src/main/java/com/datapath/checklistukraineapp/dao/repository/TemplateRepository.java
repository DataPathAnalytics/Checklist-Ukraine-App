package com.datapath.checklistukraineapp.dao.repository;

import com.datapath.checklistukraineapp.dao.domain.TemplateDomain;
import com.datapath.checklistukraineapp.dao.entity.TemplateEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;

public interface TemplateRepository extends Neo4jRepository<TemplateEntity, Long> {

    @Query(value = "match (t:Template)-->(u:User), (t:Template)-->(f:TemplateFolder) " +
            "return t, id(u) as authorId, id(f) as folderId")
    List<TemplateDomain> findTemplates();

    @Query(value = "match (t:Template)-->(u:User), (t:Template)-->(f:TemplateFolder)" +
            " where id(t) = $id" +
            " return t, id(u) as authorId, id(f) as folderId")
    TemplateDomain getTemplate(Long id);

    @Query(value = "match (c:ControlEvent)-->(t:Template), (t:Template)-->(f:TemplateFolder), (t:Template)-->(u:User)" +
            " where id(t) = $id" +
            " return t, id(u) as authorId, id(f) as folderId")
    List<TemplateDomain> findTemplatesForControlEvent(Long id);
}