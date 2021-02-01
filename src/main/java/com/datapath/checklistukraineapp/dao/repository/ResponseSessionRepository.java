package com.datapath.checklistukraineapp.dao.repository;

import com.datapath.checklistukraineapp.dao.domain.ResponseSessionDomain;
import com.datapath.checklistukraineapp.dao.entity.ResponseSessionEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;

public interface ResponseSessionRepository extends Neo4jRepository<ResponseSessionEntity, Long> {

    @Query(value = "match (t:Template)<--(rs:ResponseSession)<-[:HAS_SESSION_RESPONSE]-(c:ControlActivity), " +
            "(rs:ResponseSession)-[:HAS_REVIEWER]->(r:User), " +
            "(rs:ResponseSession)-[:HAS_AUTHOR]->(a:User), " +
            "(rs:ResponseSession)-[:IN_STATUS]->(s:SessionStatus), " +
            "where id(c)=$activityId " +
            "return c, id(t) as templateId, t.name as templateName, id(r) as reviewerId, " +
            "id(a) as authorId, s.sessionStatusId as sessionStatusId " +
            "order by rs.dateCreated, rs.name")
    List<ResponseSessionDomain> findResponseSessionByActivityId(Long activityId);
}
