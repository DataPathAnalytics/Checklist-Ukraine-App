package com.datapath.checklistapp.dao.service;

import com.datapath.checklistapp.dao.entity.TemplateEntity;
import com.datapath.checklistapp.dao.repository.TemplateRepository;
import com.datapath.checklistapp.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<TemplateEntity> findByIds(List<Long> ids) {
        return repository.findAllById(ids);
    }

    public TemplateEntity save(TemplateEntity entity) {
        return repository.save(entity);
    }
}
