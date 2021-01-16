package com.datapath.checklistukraineapp.dao.service;

import com.datapath.checklistukraineapp.dao.domain.ChecklistDomain;
import com.datapath.checklistukraineapp.dao.entity.ChecklistEntity;
import com.datapath.checklistukraineapp.dao.repository.ChecklistRepository;
import com.datapath.checklistukraineapp.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ChecklistDaoService {

    private final ChecklistRepository repository;

    public List<ChecklistDomain> findEventChecklists(Long eventId) {
        return repository.findEventChecklists(eventId);
    }

    public ChecklistEntity save(ChecklistEntity entity) {
        return repository.save(entity);
    }

    public ChecklistEntity findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("checklist", id));
    }
}
