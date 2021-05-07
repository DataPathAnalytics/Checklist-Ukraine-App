package com.datapath.checklistapp.dao.repository;

import com.datapath.checklistapp.dao.domain.ExportSessionResponseDomain;
import com.datapath.checklistapp.dao.entity.ResponseSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ResponseSessionRepository extends JpaRepository<ResponseSessionEntity, Long> {

    String RESPONSE_SESSION_DATE_LIST_REQUEST = "select rs.id as id, rs.date_modified as dateModified from response_session rs " +
            "join control_activity ca on ca.id = rs.activity_id " +
            "where ca.invalid is false and " +
            "rs.date_modified > :offset " +
            "order by rs.date_modified limit :limit";

    @Query(value = RESPONSE_SESSION_DATE_LIST_REQUEST, nativeQuery = true)
    List<ExportSessionResponseDomain> getResponseSessionDates(@Param("offset") LocalDateTime offset, @Param("limit") int limit);
}
