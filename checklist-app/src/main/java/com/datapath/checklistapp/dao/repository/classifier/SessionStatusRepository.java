package com.datapath.checklistapp.dao.repository.classifier;

import com.datapath.checklistapp.dao.entity.classifier.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionStatusRepository extends JpaRepository<SessionStatus, Integer> {
}
