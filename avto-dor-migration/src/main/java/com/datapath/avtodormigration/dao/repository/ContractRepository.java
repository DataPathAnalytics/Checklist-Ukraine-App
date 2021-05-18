package com.datapath.avtodormigration.dao.repository;

import com.datapath.avtodormigration.dao.entity.ContractEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractRepository extends JpaRepository<ContractEntity, Integer> {
}
