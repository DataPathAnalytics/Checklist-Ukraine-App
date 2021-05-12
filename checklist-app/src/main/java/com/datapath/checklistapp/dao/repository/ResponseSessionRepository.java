package com.datapath.checklistapp.dao.repository;

import com.datapath.checklistapp.dao.entity.ControlActivityEntity;
import com.datapath.checklistapp.dao.entity.SessionEntity;
import com.datapath.checklistapp.util.database.ResponseRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResponseSessionRepository extends JpaRepository<SessionEntity, Integer> {

    Page<SessionEntity> findAllByActivityAndRoleOrderByNumber(ControlActivityEntity activity, ResponseRole role, Pageable pageable);
}
