package com.datapath.checklistukraineapp.dao.service;

import com.datapath.checklistukraineapp.dao.entity.classifier.ControlStatusEntity;
import com.datapath.checklistukraineapp.dao.repository.ControlStatusRepository;
import com.datapath.checklistukraineapp.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ControlStatusDaoService {

    private final ControlStatusRepository repository;

    public List<ControlStatusEntity> findAll() {
        return repository.findAll();
    }

    public ControlStatusEntity findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("controlStatus", id));
    }
}
