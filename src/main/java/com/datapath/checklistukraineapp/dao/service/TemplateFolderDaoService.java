package com.datapath.checklistukraineapp.dao.service;

import com.datapath.checklistukraineapp.dao.entity.TemplateFolderEntity;
import com.datapath.checklistukraineapp.dao.repository.TemplateFolderRepository;
import com.datapath.checklistukraineapp.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TemplateFolderDaoService {

    private final TemplateFolderRepository repository;

    public TemplateFolderEntity save(TemplateFolderEntity entity) {
        return repository.save(entity);
    }

    public List<TemplateFolderEntity> findAll() {
        return repository.findAll();
    }

    public TemplateFolderEntity findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("folder", id));
    }
}
