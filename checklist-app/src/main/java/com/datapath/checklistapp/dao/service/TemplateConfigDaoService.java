package com.datapath.checklistapp.dao.service;

import com.datapath.checklistapp.dao.entity.TemplateConfigEntity;
import com.datapath.checklistapp.dao.entity.classifier.TemplateConfigType;
import com.datapath.checklistapp.dao.repository.TemplateConfigRepository;
import com.datapath.checklistapp.dto.request.search.SearchRequest;
import com.datapath.checklistapp.exception.EntityNotFoundException;
import com.datapath.checklistapp.util.database.Entity;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    public List<TemplateConfigEntity> findAllByType(TemplateConfigType type) {
        return repository.findAllByType(type);
    }

    public TemplateConfigEntity findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(Entity.TemplateConfig.name(), id));
    }

    public Page<TemplateConfigEntity> searchByName(SearchRequest request) {
        return repository.searchByName(
                request.getKeyword(),
                PageRequest.of(request.getPage(), request.getSize())
        );
    }

    public void delete(TemplateConfigEntity entity) {
        repository.delete(entity);
    }

    public boolean isUsed(Integer id) {
        return repository.isUsed(id);
    }
}
