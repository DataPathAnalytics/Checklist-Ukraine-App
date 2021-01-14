package com.datapath.checklistukraineapp.dao.repository;

import com.datapath.checklistukraineapp.dao.domain.ChecklistDomain;
import com.datapath.checklistukraineapp.dao.entity.ChecklistEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Optional;

public interface ChecklistRepository extends Neo4jRepository<ChecklistEntity, Long> {

    @Query(value = "match (t:Template)<--(c:ChecklistResponse)<--(e:ControlEvent), " +
            "(c:ChecklistResponse)-[:HAS_REVIEWER]->(u:User) " +
            "where id(e)=$eventId " +
            "return c, id(t) as templateId, t.name as templateName, id(u) as reviewerId " +
            "order by c.dateCreated, c.name")
    List<ChecklistDomain> findEventChecklists(Long eventId);

    @Query(value = "match (c:ChecklistResponse), (t:Template) where id(t) = $templateId and id(c) = $checklistId create (c)-[:TEMPLATED_BY]->(t)")
    void createRelationshipWithTemplate(Long checklistId, Long templateId);

    @Query(value = "match (t:Template)<--(c:ChecklistResponse), " +
            "(c:ChecklistResponse)-[:HAS_REVIEWER]->(u:User),  (c:ChecklistResponse)-[:HAS_AUTHOR]->(a:User)" +
            "where id(c)=$id " +
            "return c, id(t) as templateId, t.name as templateName, id(u) as reviewerId, id(a) as authorId")
    Optional<ChecklistDomain> findChecklist(Long id);
}
