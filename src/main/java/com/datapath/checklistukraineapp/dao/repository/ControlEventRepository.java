package com.datapath.checklistukraineapp.dao.repository;

import com.datapath.checklistukraineapp.dao.domain.ControlEventDomain;
import com.datapath.checklistukraineapp.dao.entity.ControlEventEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;

public interface ControlEventRepository extends Neo4jRepository<ControlEventEntity, Long> {

    @Query(value = "match (c:ControlEvent)-[:HAS_AUTHOR]->(u1:User), " +
            "(c:ControlEvent)-[:HAS_TEAM_LEAD]->(u2:User), " +
            "(c:ControlEvent)-->(t:ControlType), " +
            "(c:ControlEvent)-->(o:ControlObject), " +
            "(c:ControlEvent)-->(s:ControlStatus) " +
            "return c, id(u1) as authorId, id(u2) as teamLeadId, id(t) as controlTypeId, " +
            "o.name as controlObjectName, o.controlObjectId as controlObjectId, s.controlStatusId as controlStatusId")
    List<ControlEventDomain> findControlEvents();

}
