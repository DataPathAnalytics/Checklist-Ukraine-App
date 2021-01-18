package com.datapath.checklistukraineapp.dao.service;

import com.datapath.checklistukraineapp.dao.entity.classifier.FinancingTypeClassifier;
import com.datapath.checklistukraineapp.dao.repository.FinancingTypeRepository;
import com.datapath.checklistukraineapp.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FinancingTypeDaoService {

    private final FinancingTypeRepository repository;

    public List<FinancingTypeClassifier> findAll() {
        return repository.findAll();
    }

    public FinancingTypeClassifier findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("financingType", id));
    }
}
