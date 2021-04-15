package com.datapath.checklistapp.dao.service;

import com.datapath.checklistapp.dao.entity.DepartmentEntity;
import com.datapath.checklistapp.dao.repository.DepartmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DepartmentDaoService {

    private final DepartmentRepository repository;

    public DepartmentEntity findByIdentifier(Long identifier) {
        return repository.findByIdentifier(identifier);
    }
}
