package com.datapath.checklistapp.dao.repository;

import com.datapath.checklistapp.dao.entity.ControlActivityEntity;
import com.datapath.checklistapp.dao.entity.SessionEntity;
import com.datapath.checklistapp.util.database.SessionPlace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResponseSessionRepository extends JpaRepository<SessionEntity, Integer> {

    Optional<SessionEntity> findByIdAndActivity(Integer id, ControlActivityEntity activity);

    Page<SessionEntity> findAllByActivityAndPlaceOrderByNumber(ControlActivityEntity activity, SessionPlace place, Pageable pageable);
}
