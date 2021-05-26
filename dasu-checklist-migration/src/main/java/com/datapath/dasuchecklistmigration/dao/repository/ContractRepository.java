package com.datapath.dasuchecklistmigration.dao.repository;

import com.datapath.dasuchecklistmigration.dao.entity.ContractEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractRepository extends JpaRepository<ContractEntity, Integer> {
}
