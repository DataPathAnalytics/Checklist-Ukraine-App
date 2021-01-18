package com.datapath.checklistukraineapp.dao.service;

import com.datapath.checklistukraineapp.dao.entity.classifier.FinancingDirectionClassifier;
import com.datapath.checklistukraineapp.dao.repository.FinancingDirectionRepository;
import com.datapath.checklistukraineapp.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FinancingDirectionDaoService {

    private final FinancingDirectionRepository repository;

    public List<FinancingDirectionClassifier> findAll() {
        return repository.findAll();
    }

    public FinancingDirectionClassifier findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("financingDirection", id));
    }
}
