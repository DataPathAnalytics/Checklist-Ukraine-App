package com.datapath.checklistapp.dao.repository;

import com.datapath.checklistapp.dao.domain.ExportSessionResponseDomain;
import com.datapath.checklistapp.dao.entity.ResponseSessionEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ResponseSessionRepository extends Neo4jRepository<ResponseSessionEntity, Long> {

    String DATE_CREATE_REQUEST = "match (s:ResponseSession) where id(s)=$id return s.dateCreated";

    String NUMBER_REQUEST = "match (s:ResponseSession) where id(s)=$id return s.number";

    String RESPONSE_SESSION_DATE_LIST_REQUEST = "match (c:ControlActivity)-[:HAS_SESSION_RESPONSE]->(sr)-->(s:SessionStatus {sessionStatusId:2}), " +
            "(c)-[:HAS_ACTIVITY_RESPONSE]->(ar {invalid: false}) " +
            "where sr.dateModified > $offset " +
            "return sr, " +
            "sr.dateModified as dateModified " +
            "order by sr.dateModified limit $limit";

    @Query(value = DATE_CREATE_REQUEST)
    LocalDateTime getDateCreatedBySessionId(Long id);

    @Query(value = NUMBER_REQUEST)
    Integer getNumberBySessionId(Long id);

    @Query(value = RESPONSE_SESSION_DATE_LIST_REQUEST)
    List<ExportSessionResponseDomain> getResponseSessionDates(LocalDateTime offset, int limit);
}
