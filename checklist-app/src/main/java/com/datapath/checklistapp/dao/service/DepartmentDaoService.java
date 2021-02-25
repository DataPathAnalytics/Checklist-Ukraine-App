package com.datapath.checklistapp.dao.service;

import com.datapath.checklistapp.dao.entity.DepartmentEntity;
import com.datapath.checklistapp.dao.repository.DepartmentRepository;
import com.datapath.checklistapp.exception.EntityNotFoundException;
import com.datapath.checklistapp.util.database.Node;
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

    public DepartmentEntity findById(Long departmentId) {
        return repository.findById(departmentId)
                .orElseThrow(() -> new EntityNotFoundException(Node.Department.name(), departmentId));
    }
}
