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

    String SHORT_CONTROL_ACTIVITIES_QUERY = "match (c:ControlActivity)-[:HAS_AUTHOR]->(u:User), " +
            "(c:ControlActivity)-->(s:ActivityStatus), " +
            "(c:ControlActivity)-[:HAS_ACTIVITY_RESPONSE]->(r:ResponseSession) " +
            "return c, id(u) as authorId, s.activityStatusId as statusId, id(r) as activityResponseId";

    String RELATED_USER_QUERY = "match (c:ControlActivity)-->(u:User) where id(c) = $id return id(u)";

    String CONTROL_ACTIVITY_BY_USER = "match (c:ControlActivity)-[:HAS_AUTHOR]->(u:User), " +
            "(c:ControlActivity)-->(s:ActivityStatus), " +
            "(c:ControlActivity)-[:HAS_ACTIVITY_RESPONSE]->(r:ResponseSession) " +
            "where id(u) = $userId " +
            "return c, id(u) as authorId, s.activityStatusId as statusId, id(r) as activityResponseId";

    String FULL_CONTROL_ACTIVITIES_QUERY = "match (c:ControlActivity)-[:HAS_AUTHOR]->(u:User), " +
            "(c:ControlActivity)-->(s:ActivityStatus), " +
            "(c:ControlActivity)-[:HAS_ACTIVITY_RESPONSE]->(r:ResponseSession) " +
            "where id(c) = $id " +
            "optional match (c:ControlActivity)-[:HAS_SESSION_RESPONSE]->(r2:ResponseSession) " +
            "optional match (c:ControlActivity)-[:HAS_TEMPLATE]->(t:Template) " +
            "optional match (c:ControlActivity)-[:HAS_MEMBER]->(m:User) " +
            "return c, id(u) as authorId, s.activityStatusId as statusId, id(r) as activityResponseId, " +
            "collect(id(r2)) as sessionResponseIds, " +
            "collect(id(t)) as templateIds, " +
            "collect(id(m)) as memberIds";

    String UPDATED_CONTROL_ACTIVITIES = "match (c:ControlActivity)-->(r:ResponseSession), " +
            "(r:ResponseSession)-->(s:SessionStatus {sessionStatusId:2}), " +
            "(c:ControlActivity)-[:HAS_AUTHOR]->(u:User), " +
            "optional match (c:ControlActivity)-[:HAS_MEMBER]->(m:User) " +
            "where r.dateModified > $dateModified " +
            "return c, id(u) as authorId, " +
            "collect(id(r)) as sessionsIds, " +
            "collect(id(m)) as memberIds";

    @Query(value = SHORT_CONTROL_ACTIVITIES_QUERY)
    List<ControlActivityDomain> findControlActivities();

    @Query(value = RELATED_USER_QUERY)
    Set<Long> findRelatedUsers(Long id);

    @Query(value = CONTROL_ACTIVITY_BY_USER)
    List<ControlActivityDomain> findUserControlActivities(Long userId);

    @Query(value = FULL_CONTROL_ACTIVITIES_QUERY)
    Optional<ControlActivityDomain> findControlActivity(Long id);

    @Query(value = UPDATED_CONTROL_ACTIVITIES)
    List<UpdateControlActivityDomain> getUpdateControlActivities(LocalDateTime dateModified);
}
