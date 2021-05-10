package com.datapath.checklistapp.dao.service;

import com.datapath.checklistapp.dao.entity.TemplateEntity;
import com.datapath.checklistapp.dao.repository.TemplateRepository;
import com.datapath.checklistapp.dto.request.search.SearchRequest;
import com.datapath.checklistapp.exception.EntityNotFoundException;
import com.datapath.checklistapp.util.database.Entity;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class TemplateDaoService {

    private final TemplateRepository repository;

    public List<TemplateEntity> findAll() {
        return repository.findAll();
    }

    public TemplateEntity findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(Entity.Template.name(), id));
    }

    public Set<TemplateEntity> findByIds(List<Integer> ids) {
        return new HashSet<>(repository.findAllById(ids));
    }

    public TemplateEntity save(TemplateEntity entity) {
        return repository.save(entity);
    }

    public Page<TemplateEntity> searchByName(SearchRequest request) {
        return repository.searchByName(
                request.getKeyword(),
                PageRequest.of(request.getPage(), request.getSize())
        );
    }

    public void delete(TemplateEntity entity) {
        repository.delete(entity);
    }

    public boolean isUsed(Integer id) {
        return repository.isUsed(id);
    }
}
