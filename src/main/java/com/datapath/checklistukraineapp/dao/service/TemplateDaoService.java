package com.datapath.checklistukraineapp.dao.service;

import com.datapath.checklistukraineapp.dao.entity.TemplateEntity;
import com.datapath.checklistukraineapp.dao.repository.TemplateRepository;
import com.datapath.checklistukraineapp.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class TemplateDaoService {

    private final TemplateRepository repository;

    public List<TemplateEntity> findAll() {
        return repository.findAll();
    }

//    public List<TemplateEntity> findByOwnerType(OwnerType ownerType) {
//        return repository.findAllByOwnerType(ownerType);
//    }

    public TemplateEntity findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("template", id));
    }

    public List<TemplateEntity> findByIds(Set<Long> ids) {
        return repository.findAllById(ids);
    }

    public TemplateEntity save(TemplateEntity entity) {
        return repository.save(entity);
    }
}
