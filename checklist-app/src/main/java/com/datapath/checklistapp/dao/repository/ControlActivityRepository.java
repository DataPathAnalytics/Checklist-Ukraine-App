package com.datapath.checklistapp.dao.repository;

import com.datapath.checklistapp.dao.entity.ControlActivityEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ControlActivityRepository extends JpaRepository<ControlActivityEntity, Integer> {

    String CONTROL_ACTIVITIES_BY_AUTHOR = "select ca.* from control_activity ca join session rs on " +
            "ca.id = rs.activity_id " +
            "where rs.author = :authorId " +
            "and rs.role = :role " +
            "order by rs.date_modified desc";

    String CONTROL_ACTIVITIES = "select ca.* from control_activity ca join session rs on " +
            "ca.id = rs.activity_id " +
            "where rs.role = :role " +
            "order by rs.date_modified desc";

    @Query(value = CONTROL_ACTIVITIES_BY_AUTHOR, nativeQuery = true)
    Page<ControlActivityEntity> findUserControlActivities(@Param("authorId") Integer authorId, @Param("role") String role, Pageable pageable);

    @Query(value = CONTROL_ACTIVITIES, nativeQuery = true)
    Page<ControlActivityEntity> findAll(@Param("role") String role, Pageable pageable);
}
