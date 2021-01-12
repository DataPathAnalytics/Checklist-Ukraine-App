package com.datapath.checklistukraineapp.dao.repository;

import com.datapath.checklistukraineapp.dao.domain.ControlEventDomain;
import com.datapath.checklistukraineapp.dao.entity.ControlEventEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ControlEventRepository extends Neo4jRepository<ControlEventEntity, Long> {

    @Query(value = "match (c:ControlEvent)-[:HAS_AUTHOR]->(u1:User), " +
            "match (c:ControlEvent)-[:HAS_TEAM_LEAD]->(u2:User), " +
            "(c:ControlEvent)-->(t:ControlType), " +
            "(c:ControlEvent)-->(o:ControlObject), " +
            "(c:ControlEvent)-->(s:ControlStatus) " +
            "return c, id(u1) as authorId, id(u2) as teamLeadId, id(t) as controlTypeId, " +
            "o.name as controlObjectName, o.controlObjectId as controlObjectId, s.controlStatusId as controlStatusId")
    List<ControlEventDomain> findControlEvents();

    @Query(value = "match (c:ControlEvent)-[:HAS_AUTHOR]->(u1:User), " +
            "match (c:ControlEvent)-[:HAS_TEAM_LEAD]->(u2:User), " +
            "(c:ControlEvent)-->(t:ControlType), " +
            "(c:ControlEvent)-->(o:ControlObject), " +
            "(c:ControlEvent)-->(s:ControlStatus) " +
            "where id(c) = $id " +
            "return c, id(u1) as authorId, id(u2) as teamLeadId, id(t) as controlTypeId, " +
            "o.name as controlObjectName, o.controlObjectId as controlObjectId, s.controlStatusId as controlStatusId")
    Optional<ControlEventDomain> findControlEvent(Long id);

    @Query(value = "match (e:ControlEvent), (u:User) where id(e) = $eventId and id(u) in $userIds create (e)-[:HAS_MEMBER]->(u)")
    void createRelationshipWithUser(Long eventId, List<Long> userIds);

    @Query(value = "match (e:ControlEvent), (t:Template) where id(e) = $eventId and id(t) in $templateIds create (e)-[:HAS_TEMPLATE]->(t)")
    void createRelationshipWithTemplate(Long eventId, List<Long> templateIds);

    @Query(value = "match (e:ControlEvent)-[:HAS_MEMBER]->(u:User) where id(e)=$eventId return id(u)")
    Set<Long> findMembers(Long eventId);

    @Query(value = "match (e:ControlEvent)-->(u:User) where id(e)=$eventId return id(u)")
    Set<Long> findLinkedUsers(Long eventId);

    @Query(value = "match (e:ControlEvent)-[r:IN_STATUS]->(s:ControlStatus) where id(e) = $id delete r")
    void removeRelationshipWithStatus(Long id);

    @Query(value = "match (e:ControlEvent), (s:ControlStatus) where id(e) = $id and s.controlStatusId = $controlStatusId create (e)-[:IN_STATUS]->(s)")
    void createRelationshipWithStatus(Long id, Integer controlStatusId);
}
