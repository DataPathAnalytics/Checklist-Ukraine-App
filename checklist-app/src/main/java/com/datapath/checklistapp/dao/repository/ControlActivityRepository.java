package com.datapath.checklistapp.dao.repository;

import com.datapath.checklistapp.dao.domain.ExportControlActivityDomain;
import com.datapath.checklistapp.dao.entity.ControlActivityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ControlActivityRepository extends JpaRepository<ControlActivityEntity, Integer> {

    String CONTROL_ACTIVITY_BY_AUTHOR = "select ca.* from control_activity ca join response_session rs on " +
            "ca.activity_response_id = rs.id " +
            "where rs.author = :authorId";

    String CONTROL_ACTIVITY_DATE_LIST_REQUEST = "select ca.id as id, ars.date_modified as dateModified " +
            "from control_activity ca " +
            "join response_session ars on ca.activity_response_id = ars.id " +
            "where ars.date_modified > :offset and " +
            "exists (select id from response_session where activity_id = ca.id and status_id = 2)" +
            "order by ars.date_modified limit :limit";

    @Query(value = CONTROL_ACTIVITY_BY_AUTHOR, nativeQuery = true)
    List<ControlActivityEntity> findUserControlActivities(@Param("authorId") Integer authorId);

    @Query(value = CONTROL_ACTIVITY_DATE_LIST_REQUEST, nativeQuery = true)
    List<ExportControlActivityDomain> getControlActivityDates(@Param("offset") LocalDateTime offset,
                                                              @Param("limit") int limit);

}
