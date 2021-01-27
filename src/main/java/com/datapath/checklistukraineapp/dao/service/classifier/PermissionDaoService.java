package com.datapath.checklistukraineapp.dao.service.classifier;

import com.datapath.checklistukraineapp.dao.entity.classifier.Permission;
import com.datapath.checklistukraineapp.dao.repository.classifier.PermissionRepository;
import com.datapath.checklistukraineapp.exception.PermissionException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PermissionDaoService {

    private final PermissionRepository repository;

    public List<Permission> findAll() {
        return repository.findAll();
    }

    public Permission findByRole(String role) {
        return repository.findByRole(role).orElseThrow(() -> new PermissionException("Permission with role '" + role + "' not found"));
    }

    public Permission findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new PermissionException("Permission with id '" + id + "' not found"));
    }
}