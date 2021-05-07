package com.datapath.checklistapp.dao.service.classifier;

import com.datapath.checklistapp.dao.entity.classifier.Permission;
import com.datapath.checklistapp.dao.repository.classifier.PermissionRepository;
import com.datapath.checklistapp.exception.EntityNotFoundException;
import com.datapath.checklistapp.exception.PermissionException;
import com.datapath.checklistapp.util.database.Entity;
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
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(Entity.Permission.name(), id));
    }
}
