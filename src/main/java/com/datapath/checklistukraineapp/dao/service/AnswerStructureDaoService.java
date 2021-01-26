package com.datapath.checklistukraineapp.dao.service;

import com.datapath.checklistukraineapp.dao.entity.AnswerStructureEntity;
import com.datapath.checklistukraineapp.dao.repository.AnswerStructureRepository;
import com.datapath.checklistukraineapp.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AnswerStructureDaoService {

    private final AnswerStructureRepository repository;

    public AnswerStructureEntity findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("answerStructure", id));
    }

    public List<AnswerStructureEntity> findAll() {
        return repository.findAll();
    }
}
