package com.datapath.checklistukraineapp.dao.service;

import com.datapath.checklistukraineapp.dao.entity.KnowledgeCategoryEntity;
import com.datapath.checklistukraineapp.dao.repository.KnowledgeCategoryRepository;
import com.datapath.checklistukraineapp.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class KnowledgeCategoryDaoService {

    private final KnowledgeCategoryRepository repository;

    public List<KnowledgeCategoryEntity> findAll() {
        return repository.findAll();
    }

    public KnowledgeCategoryEntity findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("knowledgeCategory", id));
    }
}
