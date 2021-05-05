package com.datapath.checklistapp.dao.repository;

import com.datapath.checklistapp.dao.domain.ControlActivityDomain;
import com.datapath.checklistapp.dao.domain.ExportControlActivityDomain;
import com.datapath.checklistapp.dao.entity.ControlActivityEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ControlActivityRepository extends Neo4jRepository<ControlActivityEntity, Long> {

    String SHORT_CONTROL_ACTIVITIES_QUERY = "match (c:ControlActivity)-->(s:ActivityStatus), " +
            "(c)-[:HAS_ACTIVITY_RESPONSE]->(r) " +
            "return c, " +
            "s.activityStatusId as statusId, " +
            "id(r) as activityResponseId";

    String RELATED_USER_QUERY = "match (c:ControlActivity)-[:HAS_ACTIVITY_RESPONSE]-(r)-->(u:User) where id(c) = $id return id(u)";

    String MAX_NUMBER_QUERY = "match (c:ControlActivity)-[:HAS_SESSION_RESPONSE]->(r) where id(c)=$id return coalesce(max(r.number), 0)";

    String CONTROL_ACTIVITY_BY_USER = "match (c:ControlActivity)-->(s:ActivityStatus), " +
            "(c)-[:HAS_ACTIVITY_RESPONSE]->(r)-[:HAS_AUTHOR]->(u:User) " +
            "where id(u) = $userId " +
            "return c, " +
            "s.activityStatusId as statusId, " +
            "id(r) as activityResponseId";

    String FULL_CONTROL_ACTIVITIES_QUERY = "match (c:ControlActivity)-->(s:ActivityStatus), " +
            "(c)-[:HAS_ACTIVITY_RESPONSE]->(r)-[:HAS_AUTHOR]->(u) " +
            "where id(c) = $id " +
            "optional match (c)-[:HAS_SESSION_RESPONSE]->(r2) " +
            "optional match (c)-[:HAS_TEMPLATE]->(t) " +
            "return c, " +
            "s.activityStatusId as statusId, " +
            "id(r) as activityResponseId, " +
            "collect(id(r2)) as sessionResponseIds, " +
            "collect(id(t)) as templateIds";

    String CONTROL_ACTIVITY_DATE_LIST_REQUEST = "match (c:ControlActivity)-[:HAS_ACTIVITY_RESPONSE]->(ar) " +
            "where exists( (c)-[:HAS_SESSION_RESPONSE]->()-[:IN_STATUS]->({sessionStatusId:2}) ) " +
            "and ar.dateModified > $offset " +
            "return c, " +
            "ar.dateModified as dateModified " +
            "order by ar.dateModified limit $limit";

    String CONTROL_ACTIVITY_ID_BY_SESSION_ID = "match (rs)<-[:HES_SESSION_RESPONSE]-(c) where id(rs) = $sessionId return id(c)";

    @Query(value = SHORT_CONTROL_ACTIVITIES_QUERY)
    List<ControlActivityDomain> findControlActivities();

    @Query(value = RELATED_USER_QUERY)
    Set<Long> findRelatedUsers(Long id);

    @Query(value = CONTROL_ACTIVITY_BY_USER)
    List<ControlActivityDomain> findUserControlActivities(Long userId);

    @Query(value = FULL_CONTROL_ACTIVITIES_QUERY)
    Optional<ControlActivityDomain> findControlActivity(Long id);

    @Query(value = MAX_NUMBER_QUERY)
    Integer getSessionMaxNumber(Long id);

    @Query(value = CONTROL_ACTIVITY_DATE_LIST_REQUEST)
    List<ExportControlActivityDomain> getControlActivityDates(LocalDateTime offset, int limit);

    @Query(value = CONTROL_ACTIVITY_ID_BY_SESSION_ID)
    Long getControlActivityIdBySessionId(Long sessionId);
}
