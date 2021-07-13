package com.datapath.dasuchecklistmigration.dao.repository;

import com.datapath.dasuchecklistmigration.dao.entity.ChecklistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChecklistRepository extends JpaRepository<ChecklistEntity, Integer> {

    @Query(value = "select * from checklist where status_id in (2, 3) order by modified_date", nativeQuery = true)
    List<ChecklistEntity> findForMigrate();
}