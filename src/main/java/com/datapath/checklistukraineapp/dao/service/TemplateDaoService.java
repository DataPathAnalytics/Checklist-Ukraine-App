package com.datapath.checklistukraineapp.dao.service;

import com.datapath.checklistukraineapp.dao.domain.TemplateDomain;
import com.datapath.checklistukraineapp.dao.entity.TemplateEntity;
import com.datapath.checklistukraineapp.dao.repository.TemplateRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TemplateDaoService {

    private final TemplateRepository repository;

    public List<TemplateDomain> findAll() {
        return repository.findTemplates();
    }

    public TemplateDomain findTemplate(Long id) {
        return repository.getTemplate(id);
    }

    public void save(TemplateEntity entity) {
        repository.save(entity);
    }

    public List<TemplateDomain> findByControlEvent(Long id) {
        return repository.findTemplatesForControlEvent(id);
    }
}
