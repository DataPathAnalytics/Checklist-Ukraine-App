package com.datapath.checklistukraineapp.dao.service;

import com.datapath.checklistukraineapp.dao.entity.TemplateConfigEntity;
import com.datapath.checklistukraineapp.dao.entity.classifier.TemplateType;
import com.datapath.checklistukraineapp.dao.repository.TemplateConfigRepository;
import com.datapath.checklistukraineapp.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TemplateConfigDaoService {

    private final TemplateConfigRepository repository;

    public void save(TemplateConfigEntity entity) {
        repository.save(entity);
    }

    public List<TemplateConfigEntity> findAll() {
        return repository.findAll();
    }

    public List<TemplateConfigEntity> findAllByTemplateType(TemplateType type) {
        return repository.findAllByType(type);
    }

    public TemplateConfigEntity findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("templateConfig", id));
    }
}
