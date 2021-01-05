package com.datapath.checklistukraineapp.dao.service;

import com.datapath.checklistukraineapp.dao.entity.classifier.PermissionEntity;
import com.datapath.checklistukraineapp.dao.repository.PermissionRepository;
import com.datapath.checklistukraineapp.exception.PermissionException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PermissionDaoService {

    private final PermissionRepository repository;

    public List<PermissionEntity> findAll() {
        return repository.findAll();
    }

    public PermissionEntity findByRole(String role) {
        return repository.findByRole(role).orElseThrow(() -> new PermissionException("Permission with role '" + role + "' not found"));
    }

    public PermissionEntity findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new PermissionException("Permission with id '" + id + "' not found"));
    }
}
