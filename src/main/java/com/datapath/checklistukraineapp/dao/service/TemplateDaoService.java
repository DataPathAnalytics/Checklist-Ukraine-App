package com.datapath.checklistukraineapp.dao.service;

import com.datapath.checklistukraineapp.dao.domain.TemplateDomain;
import com.datapath.checklistukraineapp.dao.entity.TemplateEntity;
import com.datapath.checklistukraineapp.dao.repository.TemplateRepository;
import com.datapath.checklistukraineapp.exception.TemplateException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class TemplateDaoService {

    private final TemplateRepository repository;

    public List<TemplateDomain> findAll() {
        return repository.findTemplates();
    }

    public TemplateEntity findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new TemplateException(id));
    }

    public void save(TemplateEntity entity) {
        repository.save(entity);
    }

    public Set<TemplateEntity> findByIds(List<Long> templateIds) {
        return repository.findAllByIdIn(templateIds);
    }
}
