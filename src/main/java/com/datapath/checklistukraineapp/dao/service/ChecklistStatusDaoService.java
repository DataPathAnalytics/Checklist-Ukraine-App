package com.datapath.checklistukraineapp.dao.service;

import com.datapath.checklistukraineapp.dao.entity.classifier.ChecklistStatusEntity;
import com.datapath.checklistukraineapp.dao.repository.ChecklistStatusRepository;
import com.datapath.checklistukraineapp.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ChecklistStatusDaoService {

    private final ChecklistStatusRepository repository;

    public ChecklistStatusEntity findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("checklistStatus", id));
    }
}
