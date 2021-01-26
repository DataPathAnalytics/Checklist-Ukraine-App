package com.datapath.checklistukraineapp.dao.service;

import com.datapath.checklistukraineapp.dao.entity.TemplateConfigEntity;
import com.datapath.checklistukraineapp.dao.repository.TemplateConfigRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TemplateConfigDaoService {

    private final TemplateConfigRepository repository;

    public void save(TemplateConfigEntity entity) {
        repository.save(entity);
    }
}
