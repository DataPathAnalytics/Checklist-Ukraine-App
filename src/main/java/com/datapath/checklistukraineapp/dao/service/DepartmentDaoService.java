package com.datapath.checklistukraineapp.dao.service;

import com.datapath.checklistukraineapp.dao.entity.DepartmentEntity;
import com.datapath.checklistukraineapp.dao.repository.DepartmentRepository;
import com.datapath.checklistukraineapp.exception.DepartmentException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DepartmentDaoService {

    private final DepartmentRepository repository;

    public List<DepartmentEntity> findAll() {
        return repository.findAll();
    }

    public DepartmentEntity findById(Long departmentId) throws DepartmentException {
        return repository.findById(departmentId)
                .orElseThrow(() -> new DepartmentException("Department not found"));
    }
}
