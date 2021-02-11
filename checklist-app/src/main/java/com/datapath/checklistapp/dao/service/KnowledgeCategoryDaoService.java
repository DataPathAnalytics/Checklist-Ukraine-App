package com.datapath.checklistapp.dao.service;

import com.datapath.checklistapp.dao.entity.KnowledgeCategoryEntity;
import com.datapath.checklistapp.dao.repository.KnowledgeCategoryRepository;
import com.datapath.checklistapp.exception.EntityNotFoundException;
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

    public List<KnowledgeCategoryEntity> findByIds(List<Long> ids) {
        return repository.findAllById(ids);
    }
}
