package com.datapath.checklistukraineapp.dao.service;

import com.datapath.checklistukraineapp.dao.entity.TemplateConfigFolderEntity;
import com.datapath.checklistukraineapp.dao.entity.TemplateFolderEntity;
import com.datapath.checklistukraineapp.dao.repository.TemplateConfigFolderRepository;
import com.datapath.checklistukraineapp.dao.repository.TemplateFolderRepository;
import com.datapath.checklistukraineapp.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FolderDaoService {

    private final TemplateFolderRepository templateRepository;
    private final TemplateConfigFolderRepository templateConfigRepository;

    public void save(TemplateFolderEntity entity) {
        templateRepository.save(entity);
    }

    public List<TemplateFolderEntity> findAllTemplateFolders() {
        return templateRepository.findAll();
    }

    public TemplateFolderEntity findTemplateFolderById(Long id) {
        return templateRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("templateFolder", id));
    }

    public void save(TemplateConfigFolderEntity entity) {
        templateConfigRepository.save(entity);
    }

    public List<TemplateConfigFolderEntity> findAllTemplateConfigFolders() {
        return templateConfigRepository.findAll();
    }

    public TemplateConfigFolderEntity findTemplateConfigFolderById(Long id) {
        return templateConfigRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("templateConfigFolder", id));
    }
}
