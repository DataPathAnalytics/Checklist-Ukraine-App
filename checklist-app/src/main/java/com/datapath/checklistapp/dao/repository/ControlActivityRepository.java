package com.datapath.checklistapp.dao.repository;

import com.datapath.checklistapp.dao.domain.ControlActivityDomain;
import com.datapath.checklistapp.dao.domain.UpdateControlActivityDomain;
import com.datapath.checklistapp.dao.entity.ControlActivityEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ControlActivityRepository extends Neo4jRepository<ControlActivityEntity, Long> {

    @Query(value = "match (c:ControlActivity)-[:HAS_AUTHOR]->(u:User), " +
            "(c:ControlActivity)-->(s:ActivityStatus), " +
            "(c:ControlActivity)-[:HAS_ACTIVITY_RESPONSE]->(r:ResponseSession) " +
            "return c, id(u) as authorId, s.activityStatusId as statusId, id(r) as activityResponseId")
    List<ControlActivityDomain> findControlActivities();

    @Query(value = "match (c:ControlEvent)-->(u:User) where id(c) = $id return id(u)")
    Set<Long> findRelatedUsers(Long id);

    @Query(value = "match (c:ControlActivity)-[:HAS_AUTHOR]->(u:User), " +
            "(c:ControlActivity)-->(s:ActivityStatus), " +
            "(c:ControlActivity)-[:HAS_ACTIVITY_RESPONSE]->(r:ResponseSession) " +
            "where id(u) = $userId " +
            "return c, id(u) as authorId, s.activityStatusId as statusId, id(r) as activityResponseId")
    List<ControlActivityDomain> findUserControlActivities(Long userId);

    @Query(value = "match (c:ControlActivity)-[:HAS_AUTHOR]->(u:User), " +
            "(c:ControlActivity)-->(s:ActivityStatus), " +
            "(c:ControlActivity)-[:HAS_ACTIVITY_RESPONSE]->(r:ResponseSession) " +
            "where id(c) = $id " +
            "optional match (c:ControlActivity)-[:HAS_SESSION_RESPONSE]->(r2:ResponseSession) " +
            "optional match (c:ControlActivity)-[:HAS_TEMPLATE]->(t:Template) " +
            "optional match (c:ControlActivity)-[:HAS_MEMBER]->(m:User) " +
            "return c, id(u) as authorId, s.activityStatusId as statusId, id(r) as activityResponseId, " +
            "collect(id(r2)) as sessionResponseIds, " +
            "collect(id(t)) as templateIds, " +
            "collect(id(m)) as memberIds")
    Optional<ControlActivityDomain> findControlActivity(Long id);

    @Query(value = "match (c:ControlActivity)-->(r:ResponseSession), " +
            "(r:ResponseSession)-->(s:SessionStatus {sessionStatusId:2}), " +
            "(c:ControlActivity)-[:HAS_AUTHOR]->(u:User), " +
            "optional match (c:ControlActivity)-[:HAS_MEMBER]->(m:User) " +
            "where r.dateModified > $dateModified " +
            "return c, id(u) as authorId, " +
            "collect(id(r)) as sessionsIds, " +
            "collect(id(m)) as memberIds")
    List<UpdateControlActivityDomain> getUpdateControlActivities(LocalDateTime dateModified);
}
