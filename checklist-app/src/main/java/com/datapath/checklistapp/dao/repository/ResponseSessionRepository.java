package com.datapath.checklistapp.dao.repository;

import com.datapath.checklistapp.dao.domain.ExportResponseSessionDomain;
import com.datapath.checklistapp.dao.domain.ResponseSessionDomain;
import com.datapath.checklistapp.dao.entity.ResponseSessionEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ResponseSessionRepository extends Neo4jRepository<ResponseSessionEntity, Long> {

    String RESPONSE_SESSIONS_BY_ACTIVITY_ID_REQUEST = "match (t:Template)<--(rs:ResponseSession)<-[:HAS_SESSION_RESPONSE]-(c:ControlActivity), " +
            "(rs)-[:HAS_AUTHOR]->(a:User), " +
            "(rs)-[:IN_STATUS]->(s:SessionStatus) " +
            "where id(c)=$activityId " +
            "optional match (rs)-[:HAS_REVIEWER]->(r:User) " +
            "return rs, id(t) as templateId, t.name as templateName, id(r) as reviewerId, " +
            "id(a) as authorId, s.sessionStatusId as sessionStatusId " +
            "order by rs.dateCreated, rs.name";

    String DATE_CREATE_REQUEST = "match (s:ResponseSession) where id(s)=$id return s.dateCreated";

    String NUMBER_REQUEST = "match (s:ResponseSession) where id(s)=$id return s.number";

    String UPDATED_SESSIONS_REQUEST = "match (sr:ResponseSession)-->(s:SessionStatus {sessionStatusId:2}), " +
            "(sr)<-[:HAS_SESSION_RESPONSE]-(c:ControlActivity), " +
            "(ar:ResponseSession)<-[:HAS_ACTIVITY_RESPONSE]-(c) " +
            "where sr.dateModified > $dateModified" +
            "return id(sr) as sessionResponseId, " +
            "id(c) as activityId, " +
            "id(ar) as activityResponseId " +
            "order by sr.dateModified limit $limit";

    @Query(value = RESPONSE_SESSIONS_BY_ACTIVITY_ID_REQUEST)
    List<ResponseSessionDomain> findResponseSessionByActivityId(Long activityId);

    @Query(value = DATE_CREATE_REQUEST)
    LocalDateTime getDateCreatedBySessionId(Long id);

    @Query(value = NUMBER_REQUEST)
    Integer getNumberBySessionId(Long id);

    @Query(value = UPDATED_SESSIONS_REQUEST)
    List<ExportResponseSessionDomain> findForExport(LocalDateTime dateModified, int limit);
}
