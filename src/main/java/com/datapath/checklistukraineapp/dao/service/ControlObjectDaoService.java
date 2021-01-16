package com.datapath.checklistukraineapp.dao.service;

import com.datapath.checklistukraineapp.dao.entity.ControlObjectEntity;
import com.datapath.checklistukraineapp.dao.repository.ControlObjectRepository;
import com.datapath.checklistukraineapp.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ControlObjectDaoService {

    private final ControlObjectRepository repository;

    public List<ControlObjectEntity> findAll() {
        return repository.findAll();
    }

    public ControlObjectEntity findById(String id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("controlObject", id));
    }
}
