package com.datapath.checklistapp.dao.service;

import com.datapath.checklistapp.dao.entity.KnowledgeClassEntity;
import com.datapath.checklistapp.dao.repository.KnowledgeClassRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class KnowledgeClassDaoService {

    private final KnowledgeClassRepository repository;

    public Optional<KnowledgeClassEntity> findByOuterId(Long outerId) {
        return repository.findByOuterId(outerId);
    }
}
