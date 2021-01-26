package com.datapath.checklistukraineapp.dao.service;

import com.datapath.checklistukraineapp.dao.domain.ControlEventDomain;
import com.datapath.checklistukraineapp.dao.entity.ControlActivityEntity;
import com.datapath.checklistukraineapp.dao.repository.ControlActivityRepository;
import com.datapath.checklistukraineapp.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class ControlActivityDaoService {

    private final ControlActivityRepository repository;

    public List<ControlEventDomain> findAllByUser(Long userId) {
        return repository.findUserControlEvents(userId);
    }

    public List<ControlEventDomain> findAll() {
        return repository.findControlEvents();
    }

    public ControlActivityEntity save(ControlActivityEntity entity) {
        return repository.save(entity);
    }

    public ControlActivityEntity findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("controlEvent", id));
    }

    public Set<Long> findRelatedUsers(Long id) {
        return repository.findRelatedUsers(id);
    }
}
