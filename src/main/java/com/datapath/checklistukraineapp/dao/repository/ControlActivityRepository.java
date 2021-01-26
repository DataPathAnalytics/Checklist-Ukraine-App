package com.datapath.checklistukraineapp.dao.repository;

import com.datapath.checklistukraineapp.dao.domain.ControlEventDomain;
import com.datapath.checklistukraineapp.dao.entity.ControlActivityEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Set;

public interface ControlActivityRepository extends Neo4jRepository<ControlActivityEntity, Long> {

    @Query(value = "match (c:ControlEvent)-[:HAS_AUTHOR]->(u1:User), " +
            "(c:ControlEvent)-[:HAS_MEMBER]->(u2:User), " +
            "(c:ControlEvent)-->(t:ControlType), " +
            "(c:ControlEvent)-->(o:ControlObject), " +
            "(c:ControlEvent)-->(s:ControlStatus) " +
            "where (id(u1) = $id or id(u2) = $id)" +
            "return c, id(u1) as authorId, id(t) as controlTypeId, " +
            "o.name as controlObjectName, o.controlObjectId as controlObjectId, s.controlStatusId as controlStatusId "+
            "order by date, s.controlStatusId")
    List<ControlEventDomain> findUserControlEvents(Long id);

    @Query(value = "match (c:ControlEvent)-[:HAS_AUTHOR]->(u1:User), " +
            "(c:ControlEvent)-->(t:ControlType), " +
            "(c:ControlEvent)-->(o:ControlObject), " +
            "(c:ControlEvent)-->(s:ControlStatus) " +
            "return c, id(u1) as authorId, id(t) as controlTypeId, " +
            "o.name as controlObjectName, o.controlObjectId as controlObjectId, s.controlStatusId as controlStatusId "+
            "order by date, s.controlStatusId")
    List<ControlEventDomain> findControlEvents();

    @Query(value = "match (c:ControlEvent)-->(u:User) where id(c) = $id return id(u)")
    Set<Long> findRelatedUsers(Long id);
}
