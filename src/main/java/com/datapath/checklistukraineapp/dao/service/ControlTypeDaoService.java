package com.datapath.checklistukraineapp.dao.service;

import com.datapath.checklistukraineapp.dao.entity.classifier.ControlTypeEntity;
import com.datapath.checklistukraineapp.dao.repository.ControlTypeRepository;
import com.datapath.checklistukraineapp.exception.ControlTypeException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ControlTypeDaoService {

    private final ControlTypeRepository repository;

    public List<ControlTypeEntity> findAll() {
        return repository.findAll();
    }

    public ControlTypeEntity findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new ControlTypeException(id));
    }
}
