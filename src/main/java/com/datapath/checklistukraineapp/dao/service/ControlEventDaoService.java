package com.datapath.checklistukraineapp.dao.service;

import com.datapath.checklistukraineapp.dao.domain.ControlEventDomain;
import com.datapath.checklistukraineapp.dao.entity.ControlEventEntity;
import com.datapath.checklistukraineapp.dao.repository.ControlEventRepository;
import com.datapath.checklistukraineapp.exception.ControlEventException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ControlEventDaoService {

    private final ControlEventRepository repository;

    public List<ControlEventDomain> findAll() {
        return repository.findControlEvents();
    }

    public ControlEventEntity save(ControlEventEntity entity) {
        return repository.save(entity);
    }

    public ControlEventEntity findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ControlEventException(id));
    }
}
