package com.datapath.checklistapp.dao.service;

import com.datapath.checklistapp.dao.entity.InterpretationEntity;
import com.datapath.checklistapp.dao.repository.InterpretationRepository;
import com.datapath.checklistapp.exception.EntityNotFoundException;
import com.datapath.checklistapp.util.database.Node;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class InterpretationDaoService {

    private final InterpretationRepository repository;

    public InterpretationEntity findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(Node.Interpretation.name(), id));
    }

    public List<InterpretationEntity> findAll() {
        return repository.findAll();
    }
}
