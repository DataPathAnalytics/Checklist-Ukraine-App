package com.datapath.checklistapp.dao.repository;

import com.datapath.checklistapp.dao.entity.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Long> {

    DepartmentEntity findByIdentifier(Long identifier);
}
