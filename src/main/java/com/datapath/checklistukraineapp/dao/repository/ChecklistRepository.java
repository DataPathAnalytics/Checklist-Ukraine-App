package com.datapath.checklistukraineapp.dao.repository;

import com.datapath.checklistukraineapp.dao.domain.ChecklistDomain;
import com.datapath.checklistukraineapp.dao.entity.ChecklistEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;

public interface ChecklistRepository extends Neo4jRepository<ChecklistEntity, Long> {

    @Query(value = "match (t:Template)<--(c:ChecklistResponse)<--(e:ControlEvent), " +
            "(c:ChecklistResponse)-[:HAS_REVIEWER]->(u1:User), " +
            "(c:ChecklistResponse)-[:HAS_AUTHOR]->(u2:User) " +
            "where id(e)=$eventId " +
            "return c, id(t) as templateId, t.name as templateName, id(u1) as reviewerId, id(u2) as authorId " +
            "order by c.dateCreated, c.name")
    List<ChecklistDomain> findEventChecklists(Long eventId);
}
